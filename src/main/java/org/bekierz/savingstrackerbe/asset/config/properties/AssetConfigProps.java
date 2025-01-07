package org.bekierz.savingstrackerbe.asset.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "config.asset")
public record AssetConfigProps(
        Urls api,
        List<String> types
) {
    public record Urls(
            String currencyUrl,
            String cryptoUrl
    ) {
    }
}
