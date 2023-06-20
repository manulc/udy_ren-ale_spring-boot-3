package com.mlorenzo.besttravel.clients;

import com.mlorenzo.besttravel.models.responses.CurrencyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Currency;

@Component
public class CurrencyClient {
    private static final String BASE_CURRENCY_QUERY_PARAM = "?base={base}";
    private static final String SYMBOLS_CURRENCY_QUERY_PARAM = "&symbols={symbols}";
    private static final String CURRENCY_PATH = "/fixer/latest";

    private final WebClient webClient;
    private final String baseCurrency;

    public CurrencyClient(final WebClient webClient, @Value("${api.base-currency}") final String baseCurrency) {
        this.webClient = webClient;
        this.baseCurrency = baseCurrency;
    }

    public CurrencyResponse getCurrency(final Currency currency) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(CURRENCY_PATH)
                        .query(BASE_CURRENCY_QUERY_PARAM)
                        .query(SYMBOLS_CURRENCY_QUERY_PARAM)
                        .build(baseCurrency, currency.getCurrencyCode()))
                .retrieve()
                .bodyToMono(CurrencyResponse.class)
                .block();
    }
}
