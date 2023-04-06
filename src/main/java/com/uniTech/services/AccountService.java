package com.uniTech.services;

import com.uniTech.models.RequestTransfer;
import com.uniTech.models.ResponseData;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface AccountService {
    ResponseData<?> selectMyAccount(UsernamePasswordAuthenticationToken authentication);
    ResponseData<?> transfer(RequestTransfer requestTransfer);
    ResponseData<?> selectedCurrency(UsernamePasswordAuthenticationToken authentication);

}
