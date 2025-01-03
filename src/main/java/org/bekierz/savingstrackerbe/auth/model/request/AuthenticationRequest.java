package org.bekierz.savingstrackerbe.auth.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticationRequest(
    @Email
    String email,
    @NotBlank(message = "Password can not be empty")
    String password
) {
}
