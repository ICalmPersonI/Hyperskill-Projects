package budget;

public class Clothes implements Purchase {
    String product;
    double price;

    Clothes(String product, double price) {
        this.product = product;
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }
}


