package com.bala.anymind.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BitCoinWalletHistoryRestModel {

    @JsonProperty("startDatetime")
    private String startDatetime;

    @JsonProperty("endDatetime")
    private String endDatetime;

    public BitCoinWalletHistoryRestModel() {
    }

    public BitCoinWalletHistoryRestModel(String startDatetime, String endDatetime) {
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

}