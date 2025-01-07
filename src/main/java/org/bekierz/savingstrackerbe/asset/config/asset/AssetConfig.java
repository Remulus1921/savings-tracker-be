package org.bekierz.savingstrackerbe.asset.config.asset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AssetConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
