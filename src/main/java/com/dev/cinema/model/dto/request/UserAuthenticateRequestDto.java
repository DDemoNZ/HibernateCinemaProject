package com.dev.cinema.model.dto.request;

import com.dev.cinema.annotation.EmailValid;
import javax.validation.constraints.NotEmpty;

public class UserAuthenticateRequestDto {

    @NotEmpty(message = "Email must not be empty.")
    @EmailValid
    private String email;
    @NotEmpty(message = "Password must not be empty.")
    private String password;
    @NotEmpty(message = "Repeated password must not be empty.")
    private String repeatedPassword;

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
