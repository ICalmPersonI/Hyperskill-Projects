package antifraud.transaction;

import antifraud.dto.transaction.TransactionFeedback;
import antifraud.dto.transaction.TransactionResponse;
import antifraud.transaction.blacklist.BlacklistService;
import antifraud.transaction.limit.Limit;
import antifraud.transaction.limit.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionService {
    private final String ALLOWED = "ALLOWED";
    private final String PROHIBITED = "PROHIBITED";
    private final String MANUAL_PROCESSING = "MANUAL_PROCESSING";

    private BlacklistService blacklistService;

    private LimitService limitService;

    @Autowired
    private TransactionRepository repository;

    private long allowedLimit;

    private long manualLimit;

    TransactionService(BlacklistService blacklistService, LimitService limitService) {
        this.blacklistService = blacklistService;
        this.limitService = limitService;
        Optional<Limit> limit = limitService.load();
        if (limit.isPresent()) {
            allowedLimit = limit.get().getAllowed();
            manualLimit = limit.get().getManual();
        } else {
            allowedLimit = 200;
            manualLimit = 1500;
        }
    }

    TransactionResponse transaction(Transaction transaction) {
        if (transaction.getAmount() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        ArrayList<String> info = new ArrayList<>();
        List<Transaction> transactionInLastHour = repository
                .findAllByNumberAndTransactionDateBetween(
                        transaction.getNumber(),
                        transaction.getTransactionDate().minusHours(1),
                        transaction.getTransactionDate()
                );

        String responseStatus = "";

        if (blacklistService.ipExists(transaction.getIp())) {
            info.add("ip");
            responseStatus = PROHIBITED;
        }

        if (blacklistService.cardExists(transaction.getNumber())) {
            info.add("card-number");
            responseStatus = PROHIBITED;
        }

        if (transaction.getAmount() != 1000) {
            if (transaction.getAmount() > manualLimit) {
                responseStatus = PROHIBITED;
                info.add("amount");
            } else if (transaction.getAmount() > allowedLimit) {
                responseStatus = MANUAL_PROCESSING;
                info.add("amount");
            }
        }


        long differenceInRegionOverLastHour = transactionInLastHour.stream()
                .map(Transaction::getRegion)
                .distinct()
                .filter(region -> !region.equals(transaction.getRegion()))
                .count();
        long differenceInIpOverLastHour = transactionInLastHour.stream()
                .map(Transaction::getIp)
                .distinct()
                .filter(ip -> !ip.equals(transaction.getIp()))
                .count();

        if (differenceInRegionOverLastHour > 2) {
            responseStatus = PROHIBITED;
            info.add("region-correlation");
        }
        if (differenceInIpOverLastHour > 2) {
            responseStatus = PROHIBITED;
            info.add("ip-correlation");
        }

        if (differenceInRegionOverLastHour == 2) {
            responseStatus = MANUAL_PROCESSING;
            info.add("region-correlation");
        }
        if (differenceInIpOverLastHour == 2) {
            responseStatus = MANUAL_PROCESSING;
            info.add("ip-correlation");
        }

        if (responseStatus.length() == 0) {
            transaction.setResult("ALLOWED");
            repository.save(transaction);
            return new TransactionResponse(ALLOWED, "none");
        }
        Collections.sort(info);
        transaction.setResult(responseStatus);
        repository.save(transaction);
        return new TransactionResponse(responseStatus, String.join(", ", info));
    }

    Transaction setFeedback(TransactionFeedback feedback) {
        return repository.findById(feedback.getTransactionId()).map(transaction -> {
            if (transaction.getResult().equals(feedback.getFeedback())) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (transaction.getFeedback().length() == 0) {
                changeLimit(transaction.getResult(), transaction.getAmount(), feedback.getFeedback());
                transaction.setFeedback(feedback.getFeedback());
                repository.save(transaction);
                return transaction;
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    List<Transaction> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .sorted(Comparator.comparingLong(Transaction::getId))
                .collect(Collectors.toList());
    }

    List<Transaction> getAllByCardNumber(String cardNumber) {
        if (blacklistService.checkCardNumber(cardNumber)) {
            List<Transaction> transactions = repository.findAllByNumber(cardNumber);
            if (transactions.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return transactions;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void changeLimit(String transactionResult, long transactionValue, String feedback) {
        switch (feedback) {
            case ALLOWED: {
                switch (transactionResult) {
                    case MANUAL_PROCESSING: {
                        allowedLimit = increaseLimit(allowedLimit, transactionValue);
                        break;
                    }
                    case PROHIBITED: {
                        allowedLimit = increaseLimit(allowedLimit, transactionValue);
                        manualLimit = increaseLimit(manualLimit, transactionValue);
                        break;
                    }
                }
                break;
            }
            case MANUAL_PROCESSING: {
                switch (transactionResult) {
                    case ALLOWED: {
                        allowedLimit = decreaseLimit(allowedLimit, transactionValue);
                        break;
                    }
                    case PROHIBITED: {
                        manualLimit = increaseLimit(manualLimit, transactionValue);
                        break;
                    }
                }
                break;
            }

            case PROHIBITED: {
                switch (transactionResult) {
                    case ALLOWED: {
                        allowedLimit = decreaseLimit(allowedLimit, transactionValue);
                        manualLimit = decreaseLimit(manualLimit, transactionValue);
                        break;
                    }
                    case MANUAL_PROCESSING: {
                        manualLimit = decreaseLimit(manualLimit, transactionValue);
                        break;
                    }
                }
                break;
            }
        }
        limitService.save(allowedLimit, manualLimit);
    }

    private long increaseLimit(long current, long transaction) {
        return (long) Math.ceil(0.8 * (double) current + 0.2 * (double) transaction);
    }

    private long decreaseLimit(long current, long transaction) {
        return (long) Math.ceil(0.8 * (double) current - 0.2 * (double) transaction);
    }
}
