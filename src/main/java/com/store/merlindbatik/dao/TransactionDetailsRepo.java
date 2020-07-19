package com.store.merlindbatik.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.merlindbatik.entity.TransactionDetails;

public interface TransactionDetailsRepo extends JpaRepository<TransactionDetails, Integer> {
	
	@Query(value = "select * from transaction_details where transaction_id= ?1",nativeQuery = true)
	public Iterable<TransactionDetails> getTransactionDetailsByTransactionId(int transactionId);

}
