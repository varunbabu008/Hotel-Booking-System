package Server.Entity;

/**
 * Created by varunbabu on 31/3/17.
 */
public class User {
    private String username;
    private String phone;
    private String email;
    private String creditcard;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCreditcard() {
        return creditcard;
    }
    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard;
    }


}
