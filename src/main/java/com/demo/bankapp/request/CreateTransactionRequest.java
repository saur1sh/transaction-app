package com.demo.bankapp.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateTransactionRequest extends BaseRequest {

	private String username;
	private boolean isBuying;
	private String currency;
	private BigDecimal amount;

}
