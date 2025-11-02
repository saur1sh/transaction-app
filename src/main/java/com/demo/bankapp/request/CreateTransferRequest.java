package com.demo.bankapp.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateTransferRequest extends BaseRequest {

	private String senderUsername;
	private String receiverTcno;
	private String currency;
	private BigDecimal amount;

}
