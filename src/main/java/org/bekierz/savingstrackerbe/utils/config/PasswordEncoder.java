package org.bekierz.savingstrackerbe.utils.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }
}
