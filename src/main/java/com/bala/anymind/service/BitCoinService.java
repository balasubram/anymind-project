package com.bala.anymind.service;

import com.bala.anymind.dao.BitCoinComputedResultsRepository;
import com.bala.anymind.dao.BitCoinRequestRepository;
import com.bala.anymind.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class BitCoinService {

	private static final DateTimeFormatter REQUEST_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

	private static final Logger LOG = LoggerFactory.getLogger(BitCoinService.class);

	@Autowired
	private BitCoinRequestRepository bitCoinRequestRepository;

	@Autowired
	private BitCoinComputedResultsRepository bitCoinComputedResultsRepository;

	public void insertRecord(BitCoinRequestRestModel bitCoinRequestRestModel) {
		BitCoinRequestDBModel bitCoinRequestDBModel = new BitCoinRequestDBModel();
		Long timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		try {
			ZonedDateTime zonedDateTime =  ZonedDateTime.parse(bitCoinRequestRestModel.getDatetime(), REQUEST_TIME_FORMATTER);
			timestamp =  zonedDateTime.toInstant().toEpochMilli();
		} catch (DateTimeParseException dtpe) {
			LOG.error("Invalid datetime format {}", bitCoinRequestRestModel.getDatetime());
		}
		bitCoinRequestDBModel.setTimeStamp(timestamp);
		bitCoinRequestDBModel.setAmount(BigDecimal.valueOf(bitCoinRequestRestModel.getAmount()));
		bitCoinRequestRepository.createNewRecord( bitCoinRequestDBModel);
	}

	public List<BitCoinWalletHistoryResponse> getWalletHistory(BitCoinWalletHistoryRestModel bitCoinWalletHistoryRestModel) {
		try {
			Long beginTimestamp = ZonedDateTime.parse(bitCoinWalletHistoryRestModel.getStartDatetime(), REQUEST_TIME_FORMATTER).toInstant().toEpochMilli();
			Long endTimestamp  = ZonedDateTime.parse(bitCoinWalletHistoryRestModel.getEndDatetime(), REQUEST_TIME_FORMATTER).toInstant().toEpochMilli();

			List<BitCoinComputedResultsDBModel> bitCoinComputedResultsDBModels = bitCoinComputedResultsRepository.retrieveResults(beginTimestamp, endTimestamp);
			return bitCoinComputedResultsDBModels.stream().map( b -> {
				ZonedDateTime zdt = Instant.ofEpochMilli(b.getComputedTimeStamp()).atZone(ZoneId.of("UTC"));
				String datetime = zdt.format(REQUEST_TIME_FORMATTER);
				return new BitCoinWalletHistoryResponse(datetime, b.getAmount());
			}).collect(Collectors.toList());
		} catch (DateTimeParseException dtpe) {
			LOG.error("Invalid datetime format {}, {}", bitCoinWalletHistoryRestModel.getStartDatetime(), bitCoinWalletHistoryRestModel.getEndDatetime());
			return new ArrayList<>();
		}
	}

	public List<BitCoinWalletHistoryResponse> getWalletHistories() {
		List<BitCoinComputedResultsDBModel> bitCoinComputedResultsDBModels = bitCoinComputedResultsRepository.retrieveAllResults();
		return bitCoinComputedResultsDBModels.stream().map( b -> {
			ZonedDateTime zdt = Instant.ofEpochMilli(b.getComputedTimeStamp()).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
			String datetime = zdt.format(REQUEST_TIME_FORMATTER);
			return new BitCoinWalletHistoryResponse(datetime, b.getAmount());
		}).collect(Collectors.toList());
	}

}