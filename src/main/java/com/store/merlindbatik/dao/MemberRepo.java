package com.store.merlindbatik.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.merlindbatik.entity.Member;

public interface MemberRepo extends JpaRepository<Member, Integer>{
	public Optional<Member> findByUsername(String username);
	public Optional<Member> findByEmail(String email);
	public Optional<Member> findByRole(String role);
}
