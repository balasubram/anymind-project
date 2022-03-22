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
        List<BitCoinRequestDBModel> bitCoinRequestDBModels = bitCoinRequestRepository.retrieveNonProcessedBitCoinRequests(
                LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        Map<Long, List<BitCoinRequestDBModel>> groupedBitCoinRequests = bitCoinRequestDBModels.stream().collect(Collectors.groupingBy(b -> {
            ZonedDateTime zdt = Instant.ofEpochMilli(b.getTimeStamp()).atZone(ZoneId.systemDefault());
            return zdt.truncatedTo(ChronoUnit.HOURS).toInstant().toEpochMilli();
        }));
        List<BitCoinComputedResultsDBModel> bitCoinComputedResults = bitCoinComputedResultsRepository.retrieveComputedResults(groupedBitCoinRequests.keySet());
        Map<Long, BitCoinComputedResultsDBModel> bitCoinComputedResultMap = bitCoinComputedResults.stream()
                .collect(Collectors.toMap(b -> b.getComputedTimeStamp(), b -> b));
        List<BitCoinComputedResultsDBModel> toBeSaved = new ArrayList<>(groupedBitCoinRequests.size());
        for(Map.Entry<Long, List<BitCoinRequestDBModel>> groupedBitCoinEntries: groupedBitCoinRequests.entrySet()) {
            BigDecimal computedResult = groupedBitCoinEntries.getValue().stream().map(bc -> bc.getAmount())
                    .reduce((a,b) -> a.add(b)).orElse(BigDecimal.ZERO);
            BitCoinComputedResultsDBModel bitCoinComputedResultsDBModel = bitCoinComputedResultMap.getOrDefault(
                    groupedBitCoinEntries.getKey(), new BitCoinComputedResultsDBModel());
            bitCoinComputedResultsDBModel.addAmount(computedResult);
            bitCoinComputedResultsDBModel.setComputedTimeStamp(groupedBitCoinEntries.getKey());
            toBeSaved.add(bitCoinComputedResultsDBModel);
        }
        bitCoinComputedResultsRepository.createOrUpdateRecords(toBeSaved);
        bitCoinRequestDBModels.forEach(b -> b.setProcessed(true));
        bitCoinRequestRepository.updateRecords(bitCoinRequestDBModels);
    }

}