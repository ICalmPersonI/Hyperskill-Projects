package budget;

public class Food implements Purchase {
    String product;
    double price;

    Food(String product, double price) {
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
