package org.bekierz.savingstrackerbe.asset.config.asset;

import org.bekierz.savingstrackerbe.asset.config.fetcher.crypto.CryptoDataFetcher;
import org.bekierz.savingstrackerbe.asset.config.fetcher.currency.CurrencyDataFetcher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(3)
public class AssetSeeder {
    private final CurrencyDataFetcher currencyDataFetcher;
    private final CryptoDataFetcher cryptoDataFetcher;
    public AssetSeeder(CurrencyDataFetcher currencyDataFetcher, CryptoDataFetcher cryptoDataFetcher) {
        this.currencyDataFetcher = currencyDataFetcher;
        this.cryptoDataFetcher = cryptoDataFetcher;
    }

    @Bean
    public CommandLineRunner seedAssets() {
        return args -> {
            currencyDataFetcher.fetchAssets();
            cryptoDataFetcher.fetchAssets();
        };
    }
}
