package com.bala.anymind.model;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.*;

@Entity
@Table(name = "BIT_COIN_REQUESTS", schema = "BIT_COIN")
public class BitCoinRequestDBModel {

	// For Primary Key
	// Instead Used Hibernate Identity
	private static final AtomicLong PRIMARY_KEY = new AtomicLong(0);

	@Id
	@Column(name = "BIT_COIN_REQUESTS_ID", columnDefinition = "bigint", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bitCoinRequestsId;

	@Column(name = "RECEIVED_TIMESTAMP", columnDefinition = "bigint", nullable = false)
	private Long timeStamp;

	@Column(name = "AMOUNT", columnDefinition = "double", nullable = false)
	private BigDecimal amount;

	@Column(name = "PROCESSED")
	private boolean processed;

	public BitCoinRequestDBModel() {
//		this.bitCoinRequestsId = PRIMARY_KEY.addAndGet(1);
	}

	public BitCoinRequestDBModel(Long timestamp, BigDecimal amount) {
//		this.bitCoinRequestsId = PRIMARY_KEY.addAndGet(1);
		this.timeStamp = timestamp;
		this.amount = amount;
		this.processed = false;
	}

	public Long getBitCoinRequestsId() {
		return bitCoinRequestsId;
	}

	public void setBitCoinRequestsId(Long bitCoinRequestsId) {
		this.bitCoinRequestsId = bitCoinRequestsId;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean getProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

}