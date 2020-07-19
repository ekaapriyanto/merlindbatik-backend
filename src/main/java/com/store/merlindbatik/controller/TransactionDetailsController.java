package com.store.merlindbatik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.merlindbatik.dao.ProductRepo;
import com.store.merlindbatik.dao.TransactionDetailsRepo;
import com.store.merlindbatik.dao.TransactionRepo;
import com.store.merlindbatik.entity.Product;
import com.store.merlindbatik.entity.Transaction;
import com.store.merlindbatik.entity.TransactionDetails;

@RestController
@RequestMapping("/transactionDetails")
@CrossOrigin
public class TransactionDetailsController {
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private TransactionRepo transactionRepo;
	
	@Autowired
	private TransactionDetailsRepo transactionDetailsRepo;
	
	@GetMapping
	public Iterable<TransactionDetails> getAllTransactionDetails(){
		return transactionDetailsRepo.findAll();
	}
	
	@GetMapping("/checkout/{transactionId}")
	public Iterable<TransactionDetails> getTransactionDetailsById(@PathVariable int transactionId){
		return transactionDetailsRepo.getTransactionDetailsByTransactionId(transactionId);
	}
	
	@PostMapping("addTransactionDetails/{transactionId}/{productId}")
	public TransactionDetails addTransactionDetails(@RequestBody TransactionDetails transactionDetails, @PathVariable int transactionId, @PathVariable int productId) {
		Transaction findTransaction = transactionRepo.findById(transactionId).get();
		Product findProduct = productRepo.findById(productId).get();
		
		transactionDetails.setTransaction(findTransaction);
		transactionDetails.setProducts(findProduct);
		
		return transactionDetailsRepo.save(transactionDetails);
	}

}
