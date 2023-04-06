package com.uniTech.cron;

import com.google.gson.Gson;
import com.uniTech.entities.Currency;
import com.uniTech.repos.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CronCheckCurrency {
    private final CurrencyRepository currencyRepository;
    @Value("${expired.remote}")
    private boolean remote;
    @Scheduled(cron = "${check-currency.cron}")
    private void checkArriveDate() {
        try {
            if (remote) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
                HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

                ResponseEntity<String> response
                        = restTemplate.exchange("https://api.fastforex.io/fetch-multi?from=AZN&to=USD,TRY&api_key=demo", HttpMethod.GET, entity, String.class);
                Gson gson = new Gson();
                ModelCurrency result = gson.fromJson(Objects.requireNonNull(response.getBody()), ModelCurrency.class);

                Optional<Currency> currencyTRY = currencyRepository.findByName("TRY");
                if (currencyTRY.isPresent()) {
                    currencyTRY.get().setCurrency(result.getResult().getTRY());
                    currencyRepository.save(currencyTRY.get());
                } else {
                    currencyRepository.save(Currency.builder()
                            .name("TRY")
                            .currency(result.getResult().getTRY())
                            .build());
                }

                Optional<Currency> currencyUSD = currencyRepository.findByName("USD");
                if (currencyUSD.isPresent()) {
                    currencyUSD.get().setCurrency(result.getResult().getUSD());
                    currencyRepository.save(currencyUSD.get());
                } else {
                    currencyRepository.save(Currency.builder()
                            .name("USD")
                            .currency(result.getResult().getTRY())
                            .build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
