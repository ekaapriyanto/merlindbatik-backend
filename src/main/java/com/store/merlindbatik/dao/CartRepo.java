package com.store.merlindbatik.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.store.merlindbatik.entity.Cart;

public interface CartRepo extends JpaRepository<Cart, Integer>{
	
	@Query(value = "SELECT * FROM cart WHERE member_id=?",nativeQuery = true)
	public Iterable<Cart> findByMemberId(int memberId);
	
	@Query(value = "SELECT * FROM cart WHERE member_id=?1 and size=?2",nativeQuery = true)
	public Iterable<Cart> findByMemberIdAndSize(int memberId, String size);
	
	@Query(value = "SELECT * FROM cart WHERE member_id=?1", nativeQuery = true)
	public Iterable<Cart> fillCart(int userId);

}
