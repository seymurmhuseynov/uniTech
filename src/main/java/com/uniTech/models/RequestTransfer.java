package com.uniTech.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTransfer {
    private String accountNumberFrom;
    private String accountNumberTo;
    private double amount;

}
