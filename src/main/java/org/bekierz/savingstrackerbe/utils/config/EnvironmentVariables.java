package org.bekierz.savingstrackerbe.utils.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Getter
@Setter
@Component
public class EnvironmentVariables {
    @Value("${config.jwt.secret-key}")
    private String jwtSecretKey;
}
