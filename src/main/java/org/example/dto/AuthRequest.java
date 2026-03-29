package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Useremail cannot be blank")
    private String useremail;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    public AuthRequest(String useremail, String password) {
        this.useremail = useremail;
        this.password = password;
    }
}
