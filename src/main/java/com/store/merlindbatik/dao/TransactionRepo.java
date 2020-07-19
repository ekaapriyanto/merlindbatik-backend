package com.store.merlindbatik.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.merlindbatik.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Integer>{
	
	@Query(value = "select * from transaction t join transaction_details td on t.id = td.transaction_id where t.id=?1 group by t.id",nativeQuery = true)
	public Iterable<Transaction> getTransactionByTransactionId(int transactionId);
	
	@Query(value = "select * from transaction t join transaction_details td on t.id = td.transaction_id where member_id= ?1 group by transaction_id order by transaction_id desc",nativeQuery = true)
	public Iterable<Transaction> getTransactionUser(int userId);
	

//	@Query(value = "select * from transaction t join transaction_details td on t.id=td.transaction_id where member_id = ?1",nativeQuery = true)
//	public Iterable<Transaction> findTransaksiByStatus(int memberId);
}
