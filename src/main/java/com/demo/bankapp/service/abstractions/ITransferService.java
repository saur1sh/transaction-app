package com.demo.bankapp.service.abstractions;

import com.demo.bankapp.model.Transfer;

import java.util.List;

public interface ITransferService {

	Transfer createNewTransfer(Transfer transfer);
	
	List<Transfer> findAllTransfersFrom24Hours(Long userId);

}
