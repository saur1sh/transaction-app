package com.demo.bankapp.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "transfer")
public class Transfer {

	private @Id @GeneratedValue Long id;
	@Column(name = "from_user_id")
	private Long fromUserId;

	@Column(name = "to_user_id")
	private Long toUserId;

	@Column(name = "currency")
	private String currency;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "transfer_time")
	private Date transferTime;

	private Transfer() {
	}

	public Transfer(Long fromUserId, Long toUserId, String currency, BigDecimal amount) {
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.currency = currency;
		this.amount = amount;
		this.transferTime = new Date();
	}
}
