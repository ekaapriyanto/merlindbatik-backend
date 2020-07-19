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
import org.springframework.web.bind.annotation.RestController;

import com.store.merlindbatik.dao.CartRepo;
import com.store.merlindbatik.dao.MemberRepo;
import com.store.merlindbatik.dao.ProductRepo;
import com.store.merlindbatik.entity.Cart;
import com.store.merlindbatik.entity.Member;
import com.store.merlindbatik.entity.Product;

@RestController
@RequestMapping("/carts")
@CrossOrigin
public class CartController {
	
	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private MemberRepo memberRepo;
	
	@Autowired
	private ProductRepo productRepo;
	
	@GetMapping
	public Iterable<Cart>getAllCart(){
		return cartRepo.findAll();
	}
	
	//get cart by memberid
	@GetMapping("/memberCart/{memberId}")
	public Iterable<Cart> getMemberCarts(@PathVariable int memberId){
		System.out.println(memberId);
		return cartRepo.findByMemberId(memberId);
	}
	
	//get cart by member id and size
	@GetMapping("/memberCartSize/{memberId}/{size}")
	public Iterable<Cart> getMemberCartSize(@PathVariable int memberId, @PathVariable String size){
		System.out.println(size);
		return cartRepo.findByMemberIdAndSize(memberId,size);
	}
	
	//Nambah Quantty jika cart
	@PostMapping("/addToCart/{memberId}/{productId}/{size}")
	public Cart addToCart(@RequestBody Cart cart, @PathVariable int memberId, @PathVariable int productId, @PathVariable String size) {
		Product findProduct = productRepo.findById(productId).get();
		Member findMember = memberRepo.findById(memberId).get();
		cart.setSize(size);
		cart.setProduct(findProduct);
		cart.setMember(findMember);
		
		return cartRepo.save(cart);
	}
	
	//Merubah nilai quantity cart
	@PutMapping("/addQuantity/{cartId}")
	public Cart updateCartQty(@PathVariable int cartId) {
		Cart findCartData = cartRepo.findById(cartId).get();
		findCartData.setQuantity(findCartData.getQuantity()+1);
		return cartRepo.save(findCartData);
	}
	
	//DeleteCart
	@DeleteMapping("deleteCart/{cartId}")
	public void deleteCartById(@PathVariable int cartId) {
		Cart findCart = cartRepo.findById(cartId).get();
		findCart.getProduct().setCart(null);
		productRepo.save(findCart.getProduct());
		findCart.getMember().setCart(null);
		memberRepo.save(findCart.getMember());
		cartRepo.delete(findCart);
	}
	
	//cart length
	@GetMapping("fillcart/{userId}")
	public Iterable<Cart> getCartByUser(@PathVariable int userId){
		return cartRepo.fillCart(userId);
	}

}
