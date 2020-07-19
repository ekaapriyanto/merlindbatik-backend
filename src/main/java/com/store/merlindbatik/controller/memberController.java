package com.store.merlindbatik.controller;

import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.merlindbatik.dao.MemberRepo;
import com.store.merlindbatik.entity.Member;
import com.store.merlindbatik.util.EmailUtil;

@RestController
@RequestMapping("/members")
@CrossOrigin
public class memberController {

	@Autowired
	private MemberRepo memberRepo;
	
	@Autowired
	private EmailUtil emailUtil;
	
	private PasswordEncoder pwEncoder = new BCryptPasswordEncoder();
	
	//Register
	@PostMapping
	public Member registerUser(@RequestBody Member member) {
		Optional<Member>findUsername = memberRepo.findByUsername(member.getUsername());
		Optional<Member>findEmail = memberRepo.findByEmail(member.getEmail());
		
		if(findUsername.toString() != "Optional.empty") {			
			throw new RuntimeException("username exist");
		}
		if(findEmail.toString() != "Optional.empty") {			
			throw new RuntimeException("email exist");
		}
		String encodedPassword = pwEncoder.encode(member.getPassword());
		String verifyToken = pwEncoder.encode(member.getUsername() + member.getEmail());
		
		member.setPassword(encodedPassword);
		member.setVerified(false);
		//Simpan verifyToken di database
		member.setVerifyToken(verifyToken);
		member.setRole("member");
		
		Member savedMember = memberRepo.save(member);
		savedMember.setPassword(null);
		
		String linkToVerify = "http://localhost:8080/members/verify/" + member.getUsername() + "?token=" + verifyToken;
		
		String message = "<h1>Selamat! Registrasi Berhasil</h1>\n";
		message += "Akun dengan username " + member.getUsername() + " telah terdaftar!\n";
		message += "Klik <a href=\"" + linkToVerify + "\">link ini</a> untuk verifikasi email anda.";
		
		
		emailUtil.sendEmail(member.getEmail(), "Registrasi Akun", message);
		return savedMember;
	}
	
	//Verify Email
	@GetMapping("/verify/{username}")
	public String verifyUserEmail (@PathVariable String username, @RequestParam String token) {
		Member findUser = memberRepo.findByUsername(username).get();
		
		if (findUser.getVerifyToken().equals(token)) {
			findUser.setVerified(true);
		} else {
			throw new RuntimeException("Token is invalid");
		}
		
		memberRepo.save(findUser);
		
		return "Sukses!";
	}
	
	//Login 
	@GetMapping
	public Member loginUser(@RequestParam String username, @RequestParam String password) {
		Member findUser = memberRepo.findByUsername(username).get();
		
		if (pwEncoder.matches(password, findUser.getPassword())) {	
			findUser.setPassword(null);
			return findUser;
		} 
		throw new RuntimeException("Wrong Password!");
	}
	
	//userKeepLogin
	@PostMapping("/login/{id}")
	public Member userKeepLogin(@PathVariable int id) {
		Member findUser = memberRepo.findById(id).get();
		if (findUser.toString() != "Optional.empty") {
			return findUser;
		}
		throw new RuntimeException("Username dosn't Exist!");
	}
	
	//kirim email lupa password
	@GetMapping("/forgotpassword/{email}")
	public Member forgotPassword(@PathVariable String email) {
		Optional<Member> findEmail = memberRepo.findByEmail(email);
		
		if (findEmail.toString() == "Optional.empty") {
			throw new RuntimeException("email doesn't Exist!");
		}
		
		if(findEmail.get().isVerified() == true) {
			String verifyToken = pwEncoder.encode(findEmail.get().getEmail() + findEmail.get().getUsername());
			String message =  "klik link ini untuk ganti password "+ "http://localhost:3000/changepassword/" + findEmail.get().getEmail()+"/"+ verifyToken;
			emailUtil.sendEmail(findEmail.get().getEmail(), "Ganti Password", message);
			return findEmail.get();
		} 
			throw new RuntimeException("Email doesn't Verified!");
		
	}
	
	//edit password karna lupa
	@PutMapping("/changepassword")
	public Member newPassword(@RequestBody Member member) {
		Member findUsername = memberRepo.findByEmail(member.getEmail()).get();
		
		member.setId(findUsername.getId());
		member.setUsername(findUsername.getUsername());
		member.setName(findUsername.getName());
		member.setEmail(findUsername.getEmail());
		member.setPhone(findUsername.getPhone());
		member.setAddress(findUsername.getAddress());
		member.setDistrict(findUsername.getDistrict());
		member.setCity(findUsername.getCity());
		member.setProvince(findUsername.getProvince());
		member.setZipCode(findUsername.getZipCode());
		member.setRole(findUsername.getRole());
		member.setVerified(true);
		member.setVerifyToken(findUsername.getVerifyToken());
		String encodedPassword = pwEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		Member savedUser = memberRepo.save(member);
		savedUser.setPassword(null);
		return savedUser;
	}
	
	//Edit Password
	@GetMapping("/passwordchange/{userId}/{oldPassword}/{newPassword}")
	public Member passwordChange(@PathVariable int userId, @PathVariable String oldPassword, @PathVariable String newPassword) {
		Member findUser = memberRepo.findById(userId).get();
		if(pwEncoder.matches(oldPassword, findUser.getPassword())) {
			String encodedPassword = pwEncoder.encode(newPassword);
			findUser.setPassword(encodedPassword);
			Member savedUser = memberRepo.save(findUser);
			savedUser.setPassword(null);
			return savedUser;
		}
		throw new RuntimeException("Password not match");
	}
	
	//EditProfil
	@PutMapping("/editprofile")
	public Member editProfil(@RequestBody Member member) {
		Member findUsername = memberRepo.findById(member.getId()).get();
		
		String temporaryName = findUsername.getUsername();
		String temporaryEmail= findUsername.getEmail();
		findUsername.setUsername(null);
		findUsername.setEmail(null);
		
		memberRepo.save(findUsername);
		System.out.println(findUsername.getEmail());
		
		Optional<Member> usernameUsed = memberRepo.findByUsername(member.getUsername());
		if (usernameUsed.toString() != "Optional.empty") {
			findUsername.setUsername(temporaryEmail);
			findUsername.setEmail(temporaryEmail);
			memberRepo.save(findUsername);
			throw new RuntimeException("username Exist!");
		}
		
		Optional<Member> findEmail = memberRepo.findByEmail(member.getEmail());
		if (findEmail.toString() != "Optional.empty") {
			findUsername.setUsername(temporaryName);
			findUsername.setEmail(temporaryEmail);
			memberRepo.save(findUsername);
			throw new RuntimeException("Email Exist!");
		}
		
		findUsername.setUsername(member.getUsername());
		findUsername.setEmail(member.getEmail());
		memberRepo.save(findUsername);
		member.setId(findUsername.getId());
		member.setVerified(true);
		member.setRole(findUsername.getRole());
		member.setVerifyToken(findUsername.getVerifyToken());
		member.setPassword(findUsername.getPassword());
		
		Member savedMember = memberRepo.save(member);
		savedMember.setPassword(null);
		return savedMember;
	}
	
	//GetMember
	@GetMapping("/dataMember")
	public Iterable <Member> getMember(){
		return memberRepo.findAll();
	}
	
}
