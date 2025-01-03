package org.bekierz.savingstrackerbe.auth.service;

import lombok.extern.log4j.Log4j2;
import org.bekierz.savingstrackerbe.auth.model.request.AuthenticationRequest;
import org.bekierz.savingstrackerbe.auth.model.request.RegisterRequest;
import org.bekierz.savingstrackerbe.auth.model.response.AuthenticationResponse;
import org.bekierz.savingstrackerbe.user.model.CustomUserDetails;
import org.bekierz.savingstrackerbe.user.model.entity.User;
import org.bekierz.savingstrackerbe.user.repository.UserRepository;
import org.bekierz.savingstrackerbe.utils.config.PasswordEncoder;
import org.bekierz.savingstrackerbe.utils.config.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class AuthService {
    private final UserRegistryService userRegistryService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRegistryService userRegistryService,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRegistryService = userRegistryService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(RegisterRequest registerRequest) {
        User user = userRegistryService.signUp(registerRequest);
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        log.info("User authenticationprocedure started with given details: " + request.toString());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        String jwtToken = userRepository
                .findByEmail(request.email())
                .map(user -> jwtService.generateToken(new CustomUserDetails(user)))
                .orElseThrow(() -> new UsernameNotFoundException("User with email:" + request.email() + " not found"));

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

}
