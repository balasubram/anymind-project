package com.bala.anymind.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Table(name = "BIT_COIN_COMPUTED_RESULT", schema = "BIT_COIN")
public class BitCoinComputedResultsDBModel {

	private static final AtomicLong PRIMARY_KEY = new AtomicLong(0);

	@Id
	@Column(name = "BIT_COIN_COMPUTED_REQUESTS_ID", columnDefinition = "bigint", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bitCoinRequestsId;

	@Column(name = "COMPUTED_TIMESTAMP", columnDefinition = "bigint", nullable = false, unique = true)
	private Long computedTimeStamp;

	@Column(name = "AMOUNT", columnDefinition = "double", nullable = false)
	private BigDecimal amount;

	public BitCoinComputedResultsDBModel() {
		this.amount = BigDecimal.ZERO;
	}

	public BitCoinComputedResultsDBModel(Long timestamp, BigDecimal amount) {
//		this.bitCoinRequestsId = PRIMARY_KEY.addAndGet(1);
		this.computedTimeStamp = timestamp;
		this.amount = amount;
	}

	public Long getBitCoinRequestsId() {
		return bitCoinRequestsId;
	}

	public void setBitCoinRequestsId(Long bitCoinRequestsId) {
		this.bitCoinRequestsId = bitCoinRequestsId;
	}

	public Long getComputedTimeStamp() {
		return computedTimeStamp;
	}

	public void setComputedTimeStamp(Long computedTimeStamp) {
		this.computedTimeStamp = computedTimeStamp;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void addAmount(BigDecimal computedResult) {
		this.amount = amount.add(computedResult);
	}

	public void setBitCoinRequestsId() {
//		this.bitCoinRequestsId = PRIMARY_KEY.addAndGet(1);
	}

}