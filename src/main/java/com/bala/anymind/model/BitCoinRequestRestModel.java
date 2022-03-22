package com.bala.anymind.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BitCoinRequestRestModel {

    @JsonProperty("datetime")
    private String datetime;

    @JsonProperty("amount")
    private double amount;

    public BitCoinRequestRestModel() {
    }

    public BitCoinRequestRestModel(String datetime, double amount) {
        this.datetime = datetime;
        this.amount = amount;
    }

    public String getDatetime() {
        return datetime;
    }

    public double getAmount() {
        return amount;
    }

}