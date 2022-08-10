package budget;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        new Menu().menu();
    }
}

class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final User user = new User();
    private final PurchasesList<Food> food = new PurchasesList<>();
    private final PurchasesList<Clothes> clothes = new PurchasesList<>();
    private final PurchasesList<Entertainment> entertainment = new PurchasesList<>();
    private final PurchasesList<Other> other = new PurchasesList<>();

    public void menu() {
        while (true) {
            System.out.println("Choose your action:\n" +
                    "1) Add income\n" +
                    "2) Add purchase\n" +
                    "3) Show list of purchases\n" +
                    "4) Balance\n" +
                    "5) Save\n" +
                    "6) Load\n" +
                    "7) Analyze (Sort)\n" +
                    "0) Exit");

            switch (scanner.nextInt()) {
                case 1:
                    System.out.println("\nEnter income:");
                    user.addIncome(scanner.nextInt());
                    System.out.println("Income was added!\n");
                    break;
                case 2:
                    System.out.println();
                    categoriesAdd();
                    break;
                case 3:
                    System.out.println();
                    categoriesShow();
                    break;
                case 4:
                    System.out.printf("\nBalance: $%s\n\n", user.balance());
                    break;
                case 5:
                    System.out.println();
                    System.out.println("Purchases were saved!");
                    System.out.println();
                    save();
                    break;
                case 6:
                    System.out.println();
                    System.out.println("Purchases were loaded!");
                    System.out.println();
                    load();
                    break;
                case 7:
                    System.out.println();
                    analyze();
                    System.out.println();
                    break;
                case 0:
                    System.out.println("\nBye!");
                    System.exit(0);
                default:
                    System.out.println();
            }
        }
    }

    private <T extends Purchase> void analyze() {
        Scanner scanner = new Scanner(System.in);
        boolean back = false;
        while (!back) {
            System.out.println("How do you want to sort?\n" +
                    "1) Sort all purchases\n" +
                    "2) Sort by type\n" +
                    "3) Sort certain type\n" +
                    "4) Back");
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println();
                    boolean empty = false;
                    PurchasesList<Other> all = new PurchasesList<>();
                    if (!food.isEmpty()) {
                        for (int i = 0; i < food.size(); i++) {
                            all.add(new Other(food.get(i).getProduct(), food.get(i).getPrice()));
                        }
                    } else {
                        empty = true;
                    }
                    if (!entertainment.isEmpty()) {
                        for (int i = 0; i < entertainment.size(); i++) {
                            all.add(new Other(entertainment.get(i).getProduct(), entertainment.get(i).getPrice()));
                        }
                    } else {
                        empty = true;
                    }
                    if (!clothes.isEmpty()) {
                        for (int i = 0; i < clothes.size(); i++) {
                            all.add(new Other(clothes.get(i).getProduct(), clothes.get(i).getPrice()));
                        }
                    } else {
                        empty = true;
                    }
                    if (!other.isEmpty()) {
                        for (int i = 0; i < other.size(); i++) {
                            all.add(new Other(other.get(i).getProduct(), other.get(i).getPrice()));
                        }
                    } else {
                        empty = true;
                    }
                    all.sort();

                    if (!all.isEmpty()) {
                        System.out.println("All:");
                        for (int i = 0; i < all.size(); i++) {
                            System.out.printf("%s $%s\n", all.get(i).getProduct(), new DecimalFormat("###.00")
                                    .format(all.get(i).getPrice()));
                        }
                    } else  {
                        empty = true;
                    }
                    if (empty) {
                        System.out.println("The purchase list is empty!");
                    }
                    System.out.println();
                    break;
                case 2:
                    System.out.println();
                    PurchasesList<Other> list = new PurchasesList<>();
                    List<Double> sumList = new ArrayList<>();
                    if (!food.isEmpty()) {
                        double sumFood = totalSum(food);
                        sumList.add(sumFood);
                        list.add(new Other("Food", totalSum(food)));
                    } else {
                        System.out.println("Food - $0");
                    }

                    if (!entertainment.isEmpty()) {
                        double sumEntertainment = totalSum(entertainment);
                        sumList.add(sumEntertainment);
                        list.add(new Other("Entertainment", totalSum(entertainment)));
                    } else {
                        System.out.println("Entertainment - $0");
                    }

                    if (!clothes.isEmpty()) {
                        double sumClothes = totalSum(clothes);
                        sumList.add(sumClothes);
                        list.add(new Other("Clothes", totalSum(clothes)));
                    } else {
                        System.out.println("Clothes - $0");
                    }

                    if (!other.isEmpty()) {
                        double sumOther = totalSum(other);
                        sumList.add(sumOther);
                        list.add(new Other("Other", totalSum(other)));
                    } else {
                        System.out.println("Other - $0");
                    }

                    list.sort();

                    if (!list.isEmpty()) {
                        for (int i = 0; i < list.size(); i++) {
                            System.out.printf("%s - $%s\n", list.get(i).getProduct(), new DecimalFormat("###.00")
                                    .format(list.get(i).getPrice()));
                        }
                        System.out.printf("Total sum: $%s\n", new DecimalFormat("###.00")
                                .format(sumList.stream().mapToDouble(Double::doubleValue).sum()));
                    }
                    System.out.println();
                    break;
                case 3:
                    System.out.println();
                    System.out.println("Choose the type of purchase\n" +
                            "1) Food\n" +
                            "2) Clothes\n" +
                            "3) Entertainment\n" +
                            "4) Other");

                    switch (scanner.nextInt()) {
                        case 1:
                            System.out.println();
                            if (!food.isEmpty()) {
                                System.out.println("Food:");
                                food.sort();
                                for (int i = 0; i < food.size(); i++) {
                                    System.out.printf("%s $%s\n", food.get(i).getProduct(), new DecimalFormat("###.00")
                                            .format(food.get(i).getPrice()));
                                }
                            } else {
                                System.out.println("The purchase list is empty!");
                            }
                            System.out.println();
                            break;
                        case 2:
                            System.out.println();
                            if (!clothes.isEmpty()) {
                                System.out.println("Clothes:");
                                clothes.sort();
                                for (int i = 0; i < clothes.size(); i++) {
                                    System.out.printf("%s $%s\n", clothes.get(i).getProduct(), new DecimalFormat("###.00")
                                            .format(clothes.get(i).getPrice()));
                                }
                            } else {
                                System.out.println("The purchase list is empty!");
                            }
                            System.out.println();
                            break;
                        case 3:
                            System.out.println();
                            if (!entertainment.isEmpty()) {
                                System.out.println("Entertainment:");
                                entertainment.sort();
                                for (int i = 0; i < entertainment.size(); i++) {
                                    System.out.printf("%s $%s\n", entertainment.get(i).getProduct(),
                                            new DecimalFormat("###.00").format(entertainment.get(i).getPrice()));
                                }
                            } else {
                                System.out.println("The purchase list is empty!");
                            }
                            System.out.println();
                            break;
                        case 4:
                            System.out.println();
                            if (!other.isEmpty()) {
                                System.out.println("Other:");
                                other.sort();
                                for (int i = 0; i < other.size(); i++) {
                                    System.out.printf("%s $%s\n", other.get(i).getProduct(),
                                            new DecimalFormat("###.00").format(other.get(i).getPrice()));
                                }
                            } else {
                                System.out.println("The purchase list is empty!");
                            }
                            System.out.println();
                            break;
                        default:
                            System.out.println();
                    }
                    break;
                case 4:
                    //System.out.println();
                    back = true;
                    break;

            }

        }
    }

    private<T extends Purchase> double totalSum(PurchasesList<T> list) {
        List<Double> price = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            price.add(list.get(i).getPrice());
        }
        return price.stream().mapToDouble(Double::doubleValue).sum();
    }


    private void save() {
        String[] type = {"Food", "Clothes", "Entertainment", "Other"};
        try (FileOutputStream fileOutputStream = new FileOutputStream("purchases.txt", false)) {
            byte[] bytes;
            for (String line : type) {
                if (!food.isEmpty()) {
                    if (line.matches("Food")) {
                        bytes = line.getBytes();
                        fileOutputStream.write(bytes);
                        fileOutputStream.write(10);
                        for (int i = 0; i < food.size(); i++) {
                            bytes = food.get(i).getProduct().getBytes();
                            fileOutputStream.write(bytes);
                            fileOutputStream.write(62);
                            bytes = String.valueOf(food.get(i).getPrice()).getBytes();
                            fileOutputStream.write(bytes);
                            fileOutputStream.write(10);
                        }
                    }
                }
                if (!clothes.isEmpty()) {
                    if (line.matches("Clothes")) {
                        bytes = line.getBytes();
                        fileOutputStream.write(bytes);
                        fileOutputStream.write(10);
                        for (int i = 0; i < clothes.size(); i++) {
                            bytes = clothes.get(i).getProduct().getBytes();
                            fileOutputStream.write(bytes);
                            fileOutputStream.write(62);
                            bytes = String.valueOf(clothes.get(i).getPrice()).getBytes();
                            fileOutputStream.write(bytes);
                            fileOutputStream.write(10);
                        }
                    }
                }
                if (!entertainment.isEmpty()) {
                    if (line.matches("Entertainment")) {
                        bytes = line.getBytes();
                        fileOutputStream.write(bytes);
                        fileOutputStream.write(10);
                        for (int i = 0; i < entertainment.size(); i++) {
                            bytes = entertainment.get(i).getProduct().getBytes();
                            fileOutputStream.write(bytes);
                            fileOutputStream.write(62);
                            bytes = String.valueOf(entertainment.get(i).getPrice()).getBytes();
                            fileOutputStream.write(bytes);
                            fileOutputStream.write(10);
                        }
                    }
                }
                if (!other.isEmpty()) {
                    if (line.matches("Other")) {
                        bytes = line.getBytes();
                        fileOutputStream.write(bytes);
                        fileOutputStream.write(10);
                        for (int i = 0; i < other.size(); i++) {
                            bytes = other.get(i).getProduct().getBytes();
                            fileOutputStream.write(bytes);
                            fileOutputStream.write(62);
                            bytes = String.valueOf(other.get(i).getPrice()).getBytes();
                            fileOutputStream.write(bytes);
                            fileOutputStream.write(10);
                        }
                    }
                }
            }
            bytes = "Balance".getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.write(10);
            bytes = String.valueOf(user.balance()).getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.write(10);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        List<String> foodList = new ArrayList<>();
        List<String> clothesList = new ArrayList<>();
        List<String> entertainmentList = new ArrayList<>();
        List<String> otherList = new ArrayList<>();
        String balance = "";
        try (FileInputStream fileInputStream = new FileInputStream("purchases.txt")) {
            String mode = "";
            int i;
            StringBuilder stringBuilder = new StringBuilder();
            while ((i = fileInputStream.read()) != -1) {
                if (i == 10) {
                    if (stringBuilder.toString().matches("Food")) {
                        mode = "Food";
                        stringBuilder.setLength(0);
                    } else if (stringBuilder.toString().matches("Clothes")) {
                        mode = "Clothes";
                        stringBuilder.setLength(0);
                    } else if (stringBuilder.toString().matches("Entertainment")) {
                        mode = "Entertainment";
                        stringBuilder.setLength(0);
                    } else if (stringBuilder.toString().matches("Other")) {
                        mode = "Other";
                        stringBuilder.setLength(0);
                    } else if (stringBuilder.toString().matches("Balance")) {
                        mode = "Balance";
                        stringBuilder.setLength(0);
                    } else {

                        switch (mode) {
                            case "Balance":
                                balance = stringBuilder.toString();
                                stringBuilder.setLength(0);
                            case "Food":
                                foodList.add(stringBuilder.toString());
                                stringBuilder.setLength(0);
                                break;
                            case "Clothes":
                                clothesList.add(stringBuilder.toString());
                                stringBuilder.setLength(0);
                                break;
                            case "Entertainment":
                                entertainmentList.add(stringBuilder.toString());
                                stringBuilder.setLength(0);
                                break;
                            case "Other":
                                otherList.add(stringBuilder.toString());
                                stringBuilder.setLength(0);
                                break;
                        }
                    }
                } else {
                    stringBuilder.append((char) i);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < foodList.size() - 1; i++) {
            String[] split = foodList.get(i).split(">");
            food.add(new Food(split[0], Double.parseDouble(split[1])));
        }

        for (String line : clothesList) {
            String[] split = line.split(">");
            clothes.add(new Clothes(split[0], Double.parseDouble(split[1])));
        }
        for (String line : entertainmentList) {
            String[] split = line.split(">");
            entertainment.add(new Entertainment(split[0], Double.parseDouble(split[1])));
        }
        for (String line : otherList) {
            String[] split = line.split(">");
            other.add(new Other(split[0], Double.parseDouble(split[1])));
        }

        user.setIncome(Double.parseDouble(balance));
    }


    private void categoriesAdd() {
        boolean back = false;
        String[] data;
        while (!back) {
            System.out.println("Choose the type of purchase\n" +
                    "1) Food\n" +
                    "2) Clothes\n" +
                    "3) Entertainment\n" +
                    "4) Other\n" +
                    "5) Back");

            switch (scanner.nextInt()) {
                case 1:
                    scanner.skip("\n");
                    data = add();
                    food.add(new Food(data[0], Double.parseDouble(data[1])));
                    user.subIncome(Double.parseDouble(data[1]));
                    System.out.println();
                    break;
                case 2:
                    scanner.skip("\n");
                    data = add();
                    clothes.add(new Clothes(data[0], Double.parseDouble(data[1])));
                    user.subIncome(Double.parseDouble(data[1]));
                    System.out.println();
                    break;
                case 3:
                    scanner.skip("\n");
                    data = add();
                    entertainment.add(new Entertainment(data[0], Double.parseDouble(data[1])));
                    user.subIncome(Double.parseDouble(data[1]));
                    System.out.println();
                    break;
                case 4:
                    scanner.skip("\n");
                    data = add();
                    other.add(new Other(data[0], Double.parseDouble(data[1])));
                    user.subIncome(Double.parseDouble(data[1]));
                    System.out.println();
                    break;
                case 5:
                    back = true;
                    System.out.println();
                    break;
                default:
                    System.out.println();
            }
        }
    }

    private String[] add() {
        System.out.println("\nEnter purchase name:");
        String name = scanner.nextLine();
        System.out.println("Enter its price:");
        String price = scanner.nextLine();
        String[] data = new String[2];
        data[0] = name;
        data[1] = price;
        return data;
    }

    private void categoriesShow() {
        boolean back = false;
        while (!back) {
            System.out.println("Choose the type of purchase\n" +
                    "1) Food\n" +
                    "2) Clothes\n" +
                    "3) Entertainment\n" +
                    "4) Other\n" +
                    "5) All\n" +
                    "6) Back");

            switch (scanner.nextInt()) {
                case 1:
                    System.out.println("\nFood:");
                    if (!food.isEmpty()) {
                        System.out.printf("Total sum: $%s\n", String.valueOf(show(food))
                                .replaceAll(",", "."));
                    } else {
                        System.out.println("The purchase list is empty!");
                    }
                    System.out.println();
                    break;
                case 2:
                    System.out.println("\nClothes:");
                    if (!clothes.isEmpty()) {
                        System.out.printf("Total sum: $%s\n", String.valueOf(show(clothes))
                                .replaceAll(",", "."));
                    } else {
                        System.out.println("The purchase list is empty!");
                    }
                    System.out.println();
                    break;
                case 3:
                    System.out.println("\nEntertainment:");
                    if (!entertainment.isEmpty()) {
                        System.out.printf("Total sum: $%s\n", String.valueOf(show(entertainment))
                                .replaceAll(",", "."));
                    } else {
                        System.out.println("The purchase list is empty!");
                    }
                    System.out.println();
                    break;
                case 4:
                    System.out.println("\nOther:");
                    if (!other.isEmpty()) {
                        System.out.printf("Total sum: $%s\n", String.valueOf(show(other))
                                .replaceAll(",", "."));
                    } else {
                        System.out.println("The purchase list is empty!");
                    }
                    System.out.println();
                    break;
                case 5:
                    System.out.println("\nAll:");
                    if (food.isEmpty() && clothes.isEmpty() && entertainment.isEmpty() && other.isEmpty()) {
                        System.out.println("The purchase list is empty!");
                    } else {
                        List<Double> totalSum = new ArrayList<>();
                        if (!food.isEmpty()) {
                            totalSum.add(show(food));
                        }
                        if (!clothes.isEmpty()) {
                            totalSum.add(show(clothes));
                        }
                        if (!entertainment.isEmpty()) {
                            totalSum.add(show(entertainment));
                        }
                        if (!other.isEmpty()) {
                           totalSum.add(show(other));
                        }
                        if (!totalSum.isEmpty()) {
                            double sum = totalSum.stream().mapToDouble(Double::doubleValue).sum();
                            System.out.printf("Total sum: $%s\n", new DecimalFormat("####.##").format(sum));
                        }
                    }
                    System.out.println();
                    break;
                case 6:
                    back = true;
                    System.out.println();
                    break;
                default:
                    System.out.println();

            }
        }
    }

    private <T extends Purchase> double show(PurchasesList<T> list) {
        DecimalFormat decimalFormat = new DecimalFormat("###.00");
        List<Double> price = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            T elem = list.get(i);
            price.add(elem.getPrice());
            System.out.printf("%s $%s\n", elem.getProduct(), decimalFormat.format(elem.getPrice()));
        }
       return price.stream().mapToDouble(Double::doubleValue).sum();
    }
}



