package org.bekierz.savingstrackerbe.auth.service;

import lombok.extern.log4j.Log4j2;
import org.bekierz.savingstrackerbe.auth.model.request.RegisterRequest;
import org.bekierz.savingstrackerbe.user.exception.UserAlreadyExistsException;
import org.bekierz.savingstrackerbe.user.model.entity.User;
import org.bekierz.savingstrackerbe.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserRegistryService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistryService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(RegisterRequest registerRequest) {
        log.info("Trying to register user with provided email: " + registerRequest.email());
        userRepository.findByEmail(registerRequest.email())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User with provided email already exists");
                });
        User user = User.builder()
                .name(registerRequest.name())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .password(registerRequest.password())
                .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("User registered successfully");
        return userRepository.saveAndFlush(user);
    }
}
