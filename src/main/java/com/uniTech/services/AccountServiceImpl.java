package com.uniTech.services;

import com.uniTech.dto.account.ResponseAccountDTO;
import com.uniTech.dto.currency.ResponseCurrencyDTO;
import com.uniTech.entities.Account;
import com.uniTech.entities.Transactions;
import com.uniTech.entities.User;
import com.uniTech.exceptions.AccessDeniedException;
import com.uniTech.exceptions.NotAcceptable;
import com.uniTech.exceptions.NotFoundException;
import com.uniTech.models.RequestTransfer;
import com.uniTech.models.ResponseData;
import com.uniTech.repos.AccountRepository;
import com.uniTech.repos.TransferLogRepository;
import com.uniTech.repos.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransferLogRepository transferLogRepository;

    @Override
    public ResponseData<?> selectMyAccount(UsernamePasswordAuthenticationToken authentication) {
        return ResponseData.ok(accountRepository.findAllByUser_IdAndActiveTrue(getUser(authentication).getId())
                .stream()
                .map(new ResponseAccountDTO.Mapper())
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public ResponseData<?> transfer(RequestTransfer requestTransfer) {
        Account accountFrom = accountRepository.findByAccountNumberAndActiveIsTrue(requestTransfer.getAccountNumberFrom()).orElseThrow(NotFoundException::new);
        Account accountTo = accountRepository.findByAccountNumberAndActiveIsTrue(requestTransfer.getAccountNumberTo()).orElseThrow(NotFoundException::new);
        if (accountFrom.getBalance() >= requestTransfer.getAmount() && accountFrom.getBalance() > 0
                && !requestTransfer.getAccountNumberFrom().equals(requestTransfer.getAccountNumberTo())) {

            double totalAmount = BigDecimal.valueOf(
                    (requestTransfer.getAmount() / accountFrom.getCurrency().getCurrency()) * accountTo.getCurrency().getCurrency()
            ).setScale(2, RoundingMode.HALF_UP).doubleValue();

            transferLogRepository.save(Transactions.builder()
                    .accountFrom(accountFrom)
                    .accountTo(accountTo)
                    .amount(requestTransfer.getAmount())
                    .currency(accountFrom.getCurrency().getCurrency())
                    .totalAmount(totalAmount)
                    .build());

            accountFrom.setBalance(accountFrom.getBalance() - requestTransfer.getAmount());
            accountRepository.save(accountFrom);

            accountTo.setBalance(accountTo.getBalance() + totalAmount);
            accountRepository.save(accountTo);

            return ResponseData.ok();
        } else {
            throw new NotAcceptable();
        }
    }

    @Override
    public ResponseData<?> selectedCurrency(UsernamePasswordAuthenticationToken authentication) {
        return ResponseData.ok(accountRepository.findAllByUser_IdAndSelectedIsTrue(getUser(authentication).getId())
                .stream()
                .map(new ResponseCurrencyDTO.Mapper())
                .collect(Collectors.toList()));
    }

    public User getUser(UsernamePasswordAuthenticationToken authentication) {
        User user = (User) authentication.getPrincipal();
        return userRepository.findByPin(user.getPin()).orElseThrow(AccessDeniedException::new);
    }
}
