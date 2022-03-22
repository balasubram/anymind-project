package com.bala.anymind.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BitCoinWalletHistoryResponse {

    @JsonProperty("datetime")
    private String datetime;

    @JsonProperty("amount")
    private BigDecimal amount;

    public BitCoinWalletHistoryResponse() {
    }

    public BitCoinWalletHistoryResponse(String datetime, BigDecimal amount) {
        this.datetime = datetime;
        this.amount = amount;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}