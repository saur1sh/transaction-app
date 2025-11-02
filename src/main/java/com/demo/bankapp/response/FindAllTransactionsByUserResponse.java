package com.demo.bankapp.response;

import com.demo.bankapp.model.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class FindAllTransactionsByUserResponse {
	private List<Transaction> transactionList;
}
