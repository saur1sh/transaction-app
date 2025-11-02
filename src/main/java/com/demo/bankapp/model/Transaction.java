package com.demo.bankapp.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {

	private @Id @GeneratedValue Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "is_bought")
	private boolean isBought;

	@Column(name = "currency")
	private String currency;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "transaction_time")
	private Date transactionTime;

	private Transaction() {
	}

	public Transaction(Long userId, boolean isBought, String currency, BigDecimal amount) {
		this.userId = userId;
		this.isBought = isBought;
		this.currency = currency;
		this.amount = amount;
		this.transactionTime = new Date();
	}

}
