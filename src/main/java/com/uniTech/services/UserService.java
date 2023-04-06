package com.uniTech.services;

import com.uniTech.models.RequestPin;
import com.uniTech.models.ResponseData;

public interface UserService {
    ResponseData<?> register(RequestPin requestPin);
}
