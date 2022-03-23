package com.bala.anymind.job;

import com.bala.anymind.dao.BitCoinComputedResultsRepository;
import com.bala.anymind.dao.BitCoinRequestRepository;
import com.bala.anymind.model.BitCoinComputedResultsDBModel;
import com.bala.anymind.model.BitCoinRequestDBModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class BitCoinComputationJob {

    @Autowired
    private BitCoinRequestRepository bitCoinRequestRepository;

    @Autowired
    private BitCoinComputedResultsRepository bitCoinComputedResultsRepository;

    @Scheduled(cron = "${spring.anymind.bit.coin.computation.frequency}")
    public void computeBitCoinSummary() {

        // Retrieving all the not processed requests less than correct time and grouping based on the received hour
        List<BitCoinRequestDBModel> bitCoinRequestDBModels = bitCoinRequestRepository.retrieveNonProcessedBitCoinRequests(
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        if (bitCoinRequestDBModels.isEmpty()) return;
        Map<Long, List<BitCoinRequestDBModel>> groupedBitCoinRequests = bitCoinRequestDBModels.stream().collect(Collectors.groupingBy(b -> {
            ZonedDateTime zdt = Instant.ofEpochMilli(b.getTimeStamp()).atZone(ZoneId.systemDefault());
            return zdt.truncatedTo(ChronoUnit.HOURS).toInstant().toEpochMilli();
        }));

        //Minimum timestamp when the bit coin requests is received
        Long minimumTimestamp = groupedBitCoinRequests.keySet().stream().sorted().min(Long::compare).get();
        // Retrieving all the records, whose final and historical values needs to be updated
        List<BitCoinComputedResultsDBModel> computedResultsToBeModified = bitCoinComputedResultsRepository.retrieveComputedResultsGreaterThan(minimumTimestamp);
        Map<Long, BitCoinComputedResultsDBModel> computedResultsToBeModifiedMap = computedResultsToBeModified.stream()
                .collect(Collectors.toMap(b -> b.getComputedTimeStamp(), b -> b));

        // Sorting based on received timestamp
        List<Long> sortedTimestamps = computedResultsToBeModifiedMap.keySet().stream().sorted().collect(Collectors.toList());

        BigDecimal amountDifference = BigDecimal.ZERO;
        BigDecimal lastFinalAmount = BigDecimal.ZERO;
        // for each retrieved timestamp update the new amount for that hour and the final processed amount
        for (Long timeStamp : sortedTimestamps) {
            BigDecimal newSum = groupedBitCoinRequests.getOrDefault(timeStamp, new ArrayList<>()).stream().map(bc -> bc.getAmount())
                    .reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);
            amountDifference = amountDifference.add(newSum);
            BitCoinComputedResultsDBModel bitCoinComputedResultsDBModel = computedResultsToBeModifiedMap.getOrDefault(
                    timeStamp, new BitCoinComputedResultsDBModel());
            bitCoinComputedResultsDBModel.addAmount(newSum);
            bitCoinComputedResultsDBModel.addFinalAmount(amountDifference);
            lastFinalAmount = bitCoinComputedResultsDBModel.getFinalAmount();
            groupedBitCoinRequests.remove(timeStamp);
        }
        List<BitCoinComputedResultsDBModel> computedResults = new LinkedList<>(computedResultsToBeModifiedMap.values());
        for (Map.Entry<Long, List<BitCoinRequestDBModel>> groupedBitCoinEntries : groupedBitCoinRequests.entrySet()) {
            BigDecimal computedResult = groupedBitCoinEntries.getValue().stream().map(bc -> bc.getAmount())
                    .reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);
            lastFinalAmount = lastFinalAmount.add(computedResult);
            BitCoinComputedResultsDBModel bitCoinComputedResultsDBModel = new BitCoinComputedResultsDBModel();
            bitCoinComputedResultsDBModel.setComputedTimeStamp(groupedBitCoinEntries.getKey());
            bitCoinComputedResultsDBModel.addAmount(computedResult);
            bitCoinComputedResultsDBModel.addFinalAmount(lastFinalAmount);
            computedResults.add(bitCoinComputedResultsDBModel);
        }
        // Create & update processing records
        bitCoinComputedResultsRepository.createOrUpdateRecords(computedResults);
        // processing status
        bitCoinRequestDBModels.forEach(b -> b.setProcessed(true));
        bitCoinRequestRepository.updateRecords(bitCoinRequestDBModels);
    }

}