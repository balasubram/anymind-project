package com.bala.anymind.controller;


import com.bala.anymind.model.BitCoinRequestRestModel;
import com.bala.anymind.model.BitCoinWalletHistoryResponse;
import com.bala.anymind.model.BitCoinWalletHistoryRestModel;
import com.bala.anymind.service.BitCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

@RestController(BitcoinController.BASE_URL)
public class BitcoinController {

    public static final String BASE_URL = "/bitcoin";

    @Autowired
    private BitCoinService bitCoinService;

    @RequestMapping(value = "/saveRecord", method = RequestMethod.POST)
    public ResponseEntity<Serializable> saveRecord(@RequestBody BitCoinRequestRestModel bitCoinRequestRestModel) {
        bitCoinService.insertRecord(bitCoinRequestRestModel);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/getWalletHistory", method = RequestMethod.POST)
    public ResponseEntity<List<BitCoinWalletHistoryResponse>> getWalletHistory(@RequestBody BitCoinWalletHistoryRestModel bitCoinWalletHistoryRestModel) {
        List<BitCoinWalletHistoryResponse> bitCoinWalletHistoryResponses = bitCoinService.getWalletHistory(bitCoinWalletHistoryRestModel);
        return ResponseEntity.ok(bitCoinWalletHistoryResponses);
    }


    @RequestMapping(value = "/getAllWalletHistories", method = RequestMethod.POST)
    public ResponseEntity<List<BitCoinWalletHistoryResponse>> getWalletHistories() {
        List<BitCoinWalletHistoryResponse> bitCoinWalletHistoryResponses = bitCoinService.getWalletHistories();
        return ResponseEntity.ok(bitCoinWalletHistoryResponses);
    }


}
