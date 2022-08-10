package budget;

public class Other implements Purchase {
    String product;
    double price;

    Other(String product, double price) {
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
