package com.uniTech.cron;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelCurrency {
    @SerializedName("results")
    private Result result;
}
