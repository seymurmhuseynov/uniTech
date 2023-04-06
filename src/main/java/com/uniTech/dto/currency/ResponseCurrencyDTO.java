package com.uniTech.dto.currency;

import com.uniTech.entities.Account;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCurrencyDTO {
    private long id;
    private double currency;
    private String name;
    public static class Mapper implements Function<Account, ResponseCurrencyDTO> {
        @Override
        public ResponseCurrencyDTO apply(Account data) {
            return ResponseCurrencyDTO.builder()
                    .id(data.getId())
                    .currency(data.getCurrency()!=null?data.getCurrency().getCurrency():0)
                    .name(data.getCurrency()!=null?data.getCurrency().getName():"")
                    .build();
        }
    }
}
