package com.uniTech.controller;

import com.uniTech.models.RequestTransfer;
import com.uniTech.models.ResponseData;
import com.uniTech.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/uni-tech/api/v1/account")
public class AccountController {

    private final AccountService accountService;
    @GetMapping("/myAccounts")
    public ResponseData<?> myAccounts(UsernamePasswordAuthenticationToken authentication) {
        return accountService.selectMyAccount(authentication);
    }
    @PostMapping("/transfer")
    public ResponseData<?> transfer(@RequestBody RequestTransfer requestTransfer) {
        return accountService.transfer(requestTransfer);
    }
    @GetMapping("/currency")
    public ResponseData<?> selectedCurrency(UsernamePasswordAuthenticationToken authentication) {
        return accountService.selectedCurrency(authentication);
    }
}
