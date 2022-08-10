package cinema.forms.requests;

public class ReturnForm {
    private String token;

    ReturnForm() {

    }

    ReturnForm(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
