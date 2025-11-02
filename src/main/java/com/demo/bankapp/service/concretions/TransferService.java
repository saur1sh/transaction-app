package com.demo.bankapp.service.concretions;

import com.demo.bankapp.model.Transfer;
import com.demo.bankapp.repository.TransferRepository;
import com.demo.bankapp.service.abstractions.ITransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService implements ITransferService {

	private final TransferRepository repository;

	@Override
	public Transfer createNewTransfer(Transfer transfer) {
		return repository.save(transfer);
	}

	@Override
	public List<Transfer> findAllTransfersFrom24Hours(Long userId) {
		return repository.findAllTransfersFrom24Hours(userId);
	}

}
