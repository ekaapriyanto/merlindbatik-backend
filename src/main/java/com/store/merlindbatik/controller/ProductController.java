package com.store.merlindbatik.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.merlindbatik.dao.ProductRepo;
import com.store.merlindbatik.entity.Product;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
	
	@Autowired
	private ProductRepo productRepo;
	
	@GetMapping
	public Iterable <Product> getProduct(){
		return productRepo.findAll();
	}
	
	//new arrival
	@GetMapping("/new")
	public Iterable<Product> getNewArrival(){
		return productRepo.findNewArrival();
	}
	
	// Add Product
	@PostMapping("/addproducts")
	public Product addProduct(@RequestBody Product product) {
		product.setId(0);
		return productRepo.save(product);
	}
	
	// Edit Product
	@PutMapping("/editproduct/{productId}")
	public Product editProduct (@RequestBody Product product, @PathVariable int productId) {
		product.setId(productId);
		return productRepo.save(product);
	}
	
	// Delete Product
	@DeleteMapping("/deleteproduct/{id}")
	public void deleteProduct(@PathVariable int id) {
		//Optional<Product> findProduct = productRepo.findById(id);
		productRepo.deleteById(id);
	}
	
	//Get Product Details
	@GetMapping("/{id}")
	public Product productDetails(@PathVariable int id) {
		Product findProduct = productRepo.findById(id).get();
		return findProduct;
	}
	
	// Filltering
	@GetMapping("/{minPrice}/{maxPrice}/{orderBy}/{urutan}/{offset}")
	public Iterable<Product> findProductByPrice(@PathVariable double minPrice, 
			@PathVariable double maxPrice,
			@PathVariable String orderBy,
			@PathVariable String urutan,
			@RequestParam String productName,
			@RequestParam String category,
			@PathVariable int offset
			){
		if (maxPrice == 0) {
			maxPrice = 9999999;
		}
		if (orderBy.equals("productName") && urutan.equals("asc")) {
			return productRepo.findProductByPriceOrderByProductNameAsc(minPrice, maxPrice, productName, category, offset);
		}
		else if (orderBy.equals("productName") && urutan.equals("desc")) {
			return productRepo.findProductByPriceOrderByProductNameDesc(minPrice, maxPrice, productName, category, offset);
		}
		else if (orderBy.equals("sold") && urutan.equals("asc")) {
			return productRepo.findProductByPriceOrderBySoldAsc(minPrice, maxPrice, productName, category, offset);
		}
		else if (orderBy.equals("sold") && urutan.equals("desc")) {
			return productRepo.findProductByPriceOrderBySoldDesc(minPrice, maxPrice, productName, category, offset);
		}
		else if (orderBy.equals("price") && urutan.equals("asc")) {
			return productRepo.findProductByPriceOrderByPriceAsc(minPrice, maxPrice, productName, category, offset);
		}
		else {
			return productRepo.findProductByPriceOrderByPriceDesc(minPrice, maxPrice, productName, category, offset);
		}
		
	}
	
	//Count Paging
	@GetMapping("/count/{minPrice}/{maxPrice}")
	public int getCountProduct(
			@PathVariable double minPrice, 
			@PathVariable double maxPrice,
			@RequestParam String productName,
			@RequestParam String category
			) {
		if (maxPrice == 0) {
			maxPrice = 999999999;
		}
		return productRepo.getCountProduct(minPrice, maxPrice, productName, category);
	}
	
	//Report
	@GetMapping("/report/{sort}/")
	public Iterable<Product> getReportProduct(
			@PathVariable String sort,
			@RequestParam String productName,
			@RequestParam String category){
		if(sort.equals("asc")) {
			return productRepo.findProducttoReportAsc(productName, category);
		}
		else {
			return productRepo.findProducttoReportDesc(productName, category);
		}
	}
	

}
