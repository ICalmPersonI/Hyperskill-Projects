package carsharing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        String dbName = args.length > 0 ? args[1] : "database";
        new Db(dbName).createTable();
        new Application(dbName).execute();
    }
}

class Application {

    private final Scanner scanner = new Scanner(System.in);

    private final DaoImplements dao;

    Application(String dbName) {
        dao = new DaoImplements(dbName);
    }

    void execute() {
        while (true) {
            System.out.println("1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");
            switch (scanner.nextLine()) {
                case "1":
                    menuTableCompany();
                    break;
                case "2":
                    List<Record> records = dao.getAllRecords("SELECT * FROM customer;", "rented_car_id");
                    if (records.isEmpty()) {
                        System.out.println("The customer list is empty!");
                    } else {
                        String id;
                        while (true) {
                            IntStream.range(0, records.size())
                                    .forEach(f ->
                                            System.out.printf(
                                                    "%s. %s\n", new Integer(f) + 1, records.get(f).getName()
                                            )
                                    );
                            System.out.println("0. Back");
                            id = scanner.nextLine();
                            if (id.matches("0")) break;
                            try {
                                if (menuTableCustomer(dao, records.get(Integer.parseInt(id) - 1))) break;
                            } catch (IndexOutOfBoundsException | NumberFormatException ignored) {
                            }
                        }
                    }
                    break;
                case "3":
                    dao.createRecord("INSERT INTO customer(name) VALUES('%s');", "customer");
                    break;
                case "0":
                    System.exit(0);
                default:
            }
        }
    }

    private boolean menuTableCustomer(DaoImplements dao, Record customer) {
        loop:
        while (true) {
            customer = dao.getAllRecords(String.format("SELECT * FROM customer WHERE id=%s; ", customer.getId()),
                    "rented_car_id").get(0);
            System.out.println("1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" +
                    "0. Back");
            switch (scanner.nextLine()) {
                case "1":
                    if (customer.getFk_id() != 0 ) {
                        System.out.println("You've already rented a car!");
                        break loop;
                    }
                    List<Record> companyList = dao.getAllRecords("SELECT * FROM company;");
                    if (companyList.isEmpty()) {
                        System.out.println("\nThe company list is empty!\n");
                    } else {
                        Record company;
                        while (true) {
                            System.out.println("Choose a company:");
                            IntStream.range(0, companyList.size())
                                    .forEach(
                                            f -> System.out.printf(
                                                    "%s. %s\n", new Integer(f) + 1, companyList.get(f).getName()
                                            )
                                    );
                            System.out.println("0. Back");
                            String id = scanner.nextLine();
                            if (id.matches("0")) break loop;

                            try {
                                company = companyList.get(Integer.parseInt(id) - 1);
                                break;
                            } catch (ArrayIndexOutOfBoundsException ignored) {
                            }
                        }

                        List<Record> carList = dao.getAllRecords(String.format("SELECT * FROM car WHERE company_id=%s;",
                                company.getId()));
                        if (carList.isEmpty()) {
                            System.out.println("\nThe car list is empty!\n");
                        } else {
                            List<String> iteratorList = new ArrayList<>();
                            List<Record> customers = dao.getAllRecords("SELECT * FROM customer", "rented_car_id");
                            for (Record elem : customers) {
                                List<Record> temp = dao.getAllRecords(String.format("SELECT * FROM car WHERE id=%s", elem.getFk_id()));
                                if (!temp.isEmpty()) {
                                    iteratorList.add(temp.get(0).getName());
                                }
                            }

                            if (!iteratorList.isEmpty()) {
                                Iterator<Record> iterator = carList.iterator();
                                while (iterator.hasNext()) {
                                    String temp = iterator.next().getName();
                                    for (String elem : iteratorList) {
                                        if (temp.matches(elem)) iterator.remove();
                                    }
                                }
                            }
                            Record car;
                            while (true) {
                                System.out.println("Choose a car:");
                                IntStream.range(0, carList.size())
                                        .forEach(
                                                f -> System.out.printf(
                                                        "%s. %s\n", new Integer(f) + 1, carList.get(f).getName()
                                                )
                                        );

                                try {
                                    car = carList.get(scanner.nextInt() - 1);
                                    break;
                                } catch (Exception se) {
                                    se.printStackTrace();
                                }
                            }
                            dao.addCarToRecord(String.format("UPDATE customer SET rented_car_id=%s WHERE id=%s;",
                                    car.getId(), customer.getId()), car.getName());
                        }
                    }
                    break;
                case "2":
                    List<Record> checkRent = dao.getAllRecords(String.format("SELECT * FROM car WHERE id=%s;",
                            dao.getAllRecords(String.format("SELECT * FROM customer WHERE id=%s ;", customer.getId()),
                                    "rented_car_id").get(0).getFk_id()), "company_id");
                    if (checkRent.isEmpty()) {
                        System.out.println("You didn't rent a car!");
                    } else {
                        if (dao.updateRecord(String.format("UPDATE customer SET rented_car_id=NULL WHERE id=%s;",
                                customer.getId()))) {
                            System.out.println("You've returned a rented car!");
                        }
                    }
                    break;
                case "3":
                    List<Record> car = dao.getAllRecords(String.format("SELECT * FROM car WHERE id=%s;",
                            dao.getAllRecords(String.format("SELECT * FROM customer WHERE id=%s ;", customer.getId()),
                                    "rented_car_id").get(0).getFk_id()), "company_id");
                    if (car.isEmpty()) {
                        System.out.println("You didn't rent a car!");
                    } else {
                        List<Record> company = dao.getAllRecords(String.format("SELECT * FROM company WHERE id=%s",
                                car.get(0).getFk_id()));
                        System.out.println("You rented car:");
                        System.out.println(car.get(0).getName());
                        System.out.println("Company:");
                        System.out.println(company.get(0).getName());
                    }
                    break;
                case "0":
                    return true;
            }
        }
        return menuTableCustomer(dao, customer);
    }


    private void menuTableCompany() {
        while (true) {
            System.out.println("1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back");
            String choice = scanner.nextLine();
            System.out.println();
            switch (choice) {
                case "1":
                    List<Record> records = dao.getAllRecords("SELECT * FROM company;");
                    if (records.isEmpty()) {
                        System.out.println("The company list is empty!");
                    } else {
                        String id;
                        while (true) {
                            IntStream.range(0, records.size())
                                    .forEach(f ->
                                            System.out.printf(
                                                    "%s. %s\n", new Integer(f) + 1, records.get(f).getName()
                                            )
                                    );
                            System.out.println("0. Back");
                            id = scanner.nextLine();
                            if (id.matches("0")) break;
                            try {
                                if (menuTableCar(dao, records.get(Integer.parseInt(id) - 1))) break;
                            } catch (IndexOutOfBoundsException | NumberFormatException ignored) {
                            }
                        }
                    }
                    break;
                case "2":
                    dao.createRecord("INSERT INTO company(name) VALUES('%s');", "company");
                    break;
                case "0":
                    return;
                default:
            }
        }
    }


    private boolean menuTableCar(DaoImplements dao, Record company) {
        while (true) {
            System.out.printf("\n'%s' company\n", company.getName());
            System.out.println("1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");
            String choice = scanner.nextLine();
            System.out.println();
            switch (choice) {
                case "1":
                    List<Record> records = dao.getAllRecords(String.format("SELECT * FROM car WHERE company_id=%s",
                            company.getId()));
                    if (records.isEmpty()) {
                        System.out.println("\nThe car list is empty!\n");
                    } else {
                        IntStream.range(0, records.size())
                                .forEach(
                                        f -> System.out.printf(
                                                "%s. %s\n", new Integer(f) + 1, records.get(f).getName()
                                        )
                                );
                    }
                    break;
                case "2":
                    dao.createRecord("INSERT INTO car(name, company_id) VALUES('%s', " + company.getId() + ");",
                            "car");
                    break;
                case "0":
                    return true;
                default:

            }
        }
    }
}