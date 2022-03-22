package com.bala.anymind.controller;


import com.bala.anymind.model.BitCoinRequestRestModel;
import com.bala.anymind.model.BitCoinWalletHistoryResponse;
import com.bala.anymind.model.BitCoinWalletHistoryRestModel;
import com.bala.anymind.service.BitCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/getWalletHistory", method = RequestMethod.GET)
    public ResponseEntity<List<BitCoinWalletHistoryResponse>> getWalletHistory(@RequestParam("startDatetime") String startDateTime,
                                                                               @RequestParam("endDatetime") String endDatetime) {
        BitCoinWalletHistoryRestModel bitCoinWalletHistoryRestModel = new BitCoinWalletHistoryRestModel(startDateTime, endDatetime);
        List<BitCoinWalletHistoryResponse> bitCoinWalletHistoryResponses = bitCoinService.getWalletHistory(bitCoinWalletHistoryRestModel);
        return ResponseEntity.ok(bitCoinWalletHistoryResponses);
    }


    @RequestMapping(value = "/getAllWalletHistories", method = RequestMethod.GET)
    public ResponseEntity<List<BitCoinWalletHistoryResponse>> getWalletHistories() {
        List<BitCoinWalletHistoryResponse> bitCoinWalletHistoryResponses = bitCoinService.getWalletHistories();
        return ResponseEntity.ok(bitCoinWalletHistoryResponses);
    }

}
