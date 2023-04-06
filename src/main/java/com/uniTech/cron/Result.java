package com.uniTech.cron;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
    @SerializedName("USD")
    private double USD;
    @SerializedName("TRY")
    private double TRY;

}
