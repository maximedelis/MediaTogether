package tp.mediatogether.models;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationForm {

    @Size(min = 4, max = 16, message = "Username must be between 4 and 16 characters")
    @Pattern(regexp = "[ -~]*", message = "must contain printable ascii char")
    private String username;

    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    @Override
    public String toString() {
        return "Person(Name: " + this.username + ")";
    }
}
