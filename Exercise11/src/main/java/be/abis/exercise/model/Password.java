package be.abis.exercise.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class Password {

    @Size(min = 6, message = "Password must be at least 6 characters long.")
    String password;

    public Password() {
    }

    public Password(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
