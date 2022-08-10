package cinema.forms.requests;

public class PurchaseForm {
    private int row;
    private int column;

    PurchaseForm() {

    }

    PurchaseForm(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
