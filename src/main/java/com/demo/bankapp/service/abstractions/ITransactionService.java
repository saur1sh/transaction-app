package com.demo.bankapp.service.abstractions;

import com.demo.bankapp.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionService {

	Transaction createNewTransaction(Long userId, boolean isBuying, String currency, BigDecimal amount);

	int getOperationCountFromLast24Hours(Long userId);

	List<Transaction> findAllByUserId(Long userId);

}
