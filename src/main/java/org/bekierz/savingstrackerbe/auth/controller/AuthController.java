package org.bekierz.savingstrackerbe.auth.controller;

import jakarta.validation.Valid;
import org.bekierz.savingstrackerbe.auth.model.request.AuthenticationRequest;
import org.bekierz.savingstrackerbe.auth.model.request.RegisterRequest;
import org.bekierz.savingstrackerbe.auth.model.response.AuthenticationResponse;
import org.bekierz.savingstrackerbe.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public AuthenticationResponse authenticateUser(@Valid @RequestBody AuthenticationRequest authenticateRequest) {
        return authService.authenticateUser(authenticateRequest);
    }
}
