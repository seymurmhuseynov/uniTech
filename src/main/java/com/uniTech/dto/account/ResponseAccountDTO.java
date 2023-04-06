package com.uniTech.dto.account;

import com.uniTech.entities.Account;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAccountDTO {
    private long id;
    private double balance;
    private String accountNumber;
    private double currency;
    private String currencyName;
    public static class Mapper implements Function<Account, ResponseAccountDTO> {
        @Override
        public ResponseAccountDTO apply(Account data) {
            return ResponseAccountDTO.builder()
                    .id(data.getId())
                    .balance(data.getBalance())
                    .accountNumber(data.getAccountNumber())
                    .currency(data.getCurrency()!=null?data.getCurrency().getCurrency():0)
                    .currencyName(data.getCurrency()!=null?data.getCurrency().getName():"")
                    .build();
        }
    }
}
