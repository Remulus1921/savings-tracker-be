package org.bekierz.savingstrackerbe.asset.config.seeder;

import org.bekierz.savingstrackerbe.asset.config.fetcher.crypto.CryptoDataFetcher;
import org.bekierz.savingstrackerbe.asset.config.fetcher.currency.CurrencyDataFetcher;
import org.bekierz.savingstrackerbe.asset.config.fetcher.type.AssetTypeFetcher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Seeder {
    private final CurrencyDataFetcher currencyDataFetcher;
    private final CryptoDataFetcher cryptoDataFetcher;
    private final AssetTypeFetcher assetTypeFetcher;

    public Seeder(CurrencyDataFetcher currencyDataFetcher,
                  CryptoDataFetcher cryptoDataFetcher,
                  AssetTypeFetcher assetTypeFetcher
    ) {
        this.currencyDataFetcher = currencyDataFetcher;
        this.cryptoDataFetcher = cryptoDataFetcher;
        this.assetTypeFetcher = assetTypeFetcher;
    }

    @Bean
    public CommandLineRunner seedAssets() {
        return args -> {
            assetTypeFetcher.fetchTypes();
            currencyDataFetcher.fetchAssets();
            //cryptoDataFetcher.fetchAssets();
        };
    }
}
