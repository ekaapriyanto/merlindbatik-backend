package com.store.merlindbatik.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.merlindbatik.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{
	public Product findByProductName(String productName);
	
	//New Arival
	@Query(value = "select * from product where id order by id desc limit 4",nativeQuery = true)
	public Iterable<Product> findNewArrival();
	
	//FIltering
	@Query(value = "select * from product where price >=?1 and price <= ?2 and product_name like %?3% and category like %?4% order by product_name asc limit 9 offset ?5",nativeQuery = true)
	public Iterable<Product> findProductByPriceOrderByProductNameAsc(double minPrice, double maxPrice, String productName, String category, int offset);
	
	@Query(value = "select * from product where price >=?1 and price <= ?2 and product_name like %?3% and category like %?4% order by product_name desc limit 9 offset ?5",nativeQuery = true)
	public Iterable<Product> findProductByPriceOrderByProductNameDesc(double minPrice, double maxPrice, String productName, String category, int offset);
	
	@Query(value = "select * from product where price >=?1 and price <= ?2 and product_name like %?3% and category like %?4% order by price asc limit 9 offset ?5",nativeQuery = true)
	public Iterable<Product> findProductByPriceOrderByPriceAsc(double minPrice, double maxPrice, String productName, String category, int offset);
	
	@Query(value = "select * from product where price >=?1 and price <= ?2 and product_name like %?3% and category like %?4% order by price desc limit 9 offset ?5",nativeQuery = true)
	public Iterable<Product> findProductByPriceOrderByPriceDesc(double minPrice, double maxPrice, String productName, String category, int offset);

	@Query(value = "select * from product where price >=?1 and price <= ?2 and product_name like %?3% and category like %?4% order by sold asc limit 9 offset ?5",nativeQuery = true)
	public Iterable<Product> findProductByPriceOrderBySoldAsc(double minPrice, double maxPrice, String productName, String category, int offset);
	
	@Query(value = "select * from product where price >=?1 and price <= ?2 and product_name like %?3% and category like %?4% order by sold desc limit 9 offset ?5",nativeQuery = true)
	public Iterable<Product> findProductByPriceOrderBySoldDesc(double minPrice, double maxPrice, String productName, String category, int offset);

	//count product ALL
	@Query(value = "select count(*) from  product where price>=?1 and price<= ?2 and product_name like %?3% and category like %?4%",nativeQuery = true)
	public int getCountProduct(double minPrice, double maxPrice, String productName, String category);
	
	//Report
	@Query(value = "Select * from product where sold and product_name like %?1% and category like %?2% order by sold asc",nativeQuery = true)
	public Iterable<Product> findProducttoReportAsc(String productName, String category);
	
	@Query(value = "Select * from product where sold and product_name like %?1% and category like %?2% order by sold desc",nativeQuery = true)
	public Iterable<Product> findProducttoReportDesc(String productName, String category);
}
