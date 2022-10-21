package budget;

public class Entertainment implements Purchase {
    String product;
    double price;

    Entertainment(String product, double price) {
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
