package antifraud.transaction.blacklist;

import antifraud.transaction.blacklist.card.Card;
import antifraud.transaction.blacklist.card.CardRepository;
import antifraud.transaction.blacklist.ip.IP;
import antifraud.transaction.blacklist.ip.IPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class BlacklistService {

    private final Pattern ipPattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    private CardRepository cardRepository;
    private IPRepository ipRepository;

    @Autowired
    BlacklistService(CardRepository cardRepository, IPRepository ipRepository) {
        this.cardRepository = cardRepository;
        this.ipRepository = ipRepository;
    }

    public boolean ipExists(String ip) {
        return ipRepository.existsByIp(ip);
    }

    IP saveIP(IP ip) {
        if (!ip.getIp().matches(ipPattern.pattern())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (ipRepository.existsByIp(ip.getIp())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return ipRepository.save(ip);
    }

    void deleteIP(String ip) {
        if (!ip.matches(ipPattern.pattern())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<IP> ipEntity = ipRepository.findByIp(ip);
        if (ipEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ipRepository.delete(ipEntity.get());
    }

    List<IP> getAllIps() {
        return StreamSupport.stream(ipRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public boolean cardExists(String cardNumber) {
        return cardRepository.existsByNumber(cardNumber);
    }

    Card saveCard(Card card) {
        if (!checkCardNumber(card.getNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (cardRepository.existsByNumber(card.getNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return cardRepository.save(card);
    }

    void deleteCard(String cardNumber) {
        if (!checkCardNumber(cardNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Card> cardEntity = cardRepository.findByNumber(cardNumber);
        if (cardEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        cardRepository.delete(cardEntity.get());
    }

    List<Card> getAllCards() {
        return StreamSupport.stream(cardRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public boolean checkCardNumber(String cardNumber) {
        int creditCardNumLength = 16;
        if (cardNumber.length() != creditCardNumLength) {
            return false;
        }
        int[] number = Arrays.stream(cardNumber.split("")).mapToInt(Integer::parseInt).toArray();
        int sum = number[creditCardNumLength - 1];
        for (int i = 0; i < creditCardNumLength - 1; i++) {
            if (i % 2 == 0) {
                int temp = number[i] * 2;
                sum += temp > 9 ? temp - 9 : temp;
            } else {
                sum += number[i];
            }
        }
        return sum % 10 == 0;
    }

}
