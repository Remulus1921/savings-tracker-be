package org.bekierz.savingstrackerbe.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegisterRequest(
    @Email
    @NotBlank(message = "Email can not be empty")
    String email,
    @NotBlank(message = "Password can not be empty")
    String password,
    @NotBlank(message = "Name can not be empty")
    String name,
    @NotBlank(message = "Surname can not be empty")
    String lastName
) {
}
