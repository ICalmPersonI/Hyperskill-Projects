package budget;

public class User {
    private double income = 0;

    public void setIncome(double income) {
        this.income = income;
    }

    public void addIncome(double income) {
        this.income = this.income + income;
    }

    public void subIncome(double income) {
        this.income = this.income - income;
    }

    public double balance() {
        return income;
    }
}
