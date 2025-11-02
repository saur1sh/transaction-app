package com.demo.bankapp.repository;

import com.demo.bankapp.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface TransferRepository extends JpaRepository<Transfer, Long> {

	@Query(value = "SELECT t FROM Transfer t WHERE t.fromUserId = :userId and t.transferTime >= DATEADD(day, -1, GETDATE())")
	List<Transfer> findAllTransfersFrom24Hours(@Param("userId") Long userId);

}
