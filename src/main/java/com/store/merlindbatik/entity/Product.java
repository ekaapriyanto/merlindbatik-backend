package com.store.merlindbatik.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String productName;
	private double price;
	private String category;
	private int stockSizeS;
	private int stockSizeM;
	private int stockSizeL;
	private String description;
	private String image;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getStockSizeS() {
		return stockSizeS;
	}
	public void setStockSizeS(int stockSizeS) {
		this.stockSizeS = stockSizeS;
	}
	public int getStockSizeM() {
		return stockSizeM;
	}
	public void setStockSizeM(int stockSizeM) {
		this.stockSizeM = stockSizeM;
	}
	public int getStockSizeL() {
		return stockSizeL;
	}
	public void setStockSizeL(int stockSizeL) {
		this.stockSizeL = stockSizeL;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
	
}
