package com.uniTech.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseJWT {
    private String token;
    private String type = "Bearer";
}
