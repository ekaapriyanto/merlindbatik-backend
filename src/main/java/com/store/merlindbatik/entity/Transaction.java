package com.store.merlindbatik.entity;

import java.util.List;

import javax.persistence.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int totalPrice;
	private String jasaPengiriman;
	private String tanggalBeli;
	private String tanggalAcc;
	private String buktiTransfer;
	private String status;
	
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "member_id")
	private Member member;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transaction", cascade = CascadeType.ALL)
	private List<TransactionDetails> transactionDetails;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getJasaPengiriman() {
		return jasaPengiriman;
	}

	public void setJasaPengiriman(String jasaPengiriman) {
		this.jasaPengiriman = jasaPengiriman;
	}

	public String getTanggalBeli() {
		return tanggalBeli;
	}

	public void setTanggalBeli(String tanggalBeli) {
		this.tanggalBeli = tanggalBeli;
	}

	public String getTanggalAcc() {
		return tanggalAcc;
	}

	public void setTanggalAcc(String tanggalAcc) {
		this.tanggalAcc = tanggalAcc;
	}

	public String getBuktiTransfer() {
		return buktiTransfer;
	}

	public void setBuktiTransfer(String buktiTransfer) {
		this.buktiTransfer = buktiTransfer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<TransactionDetails> getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(List<TransactionDetails> transactionDetails) {
		this.transactionDetails = transactionDetails;
	}
	
	
	
}
