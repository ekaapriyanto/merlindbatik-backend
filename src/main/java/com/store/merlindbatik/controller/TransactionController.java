package com.store.merlindbatik.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.store.merlindbatik.dao.MemberRepo;
import com.store.merlindbatik.dao.TransactionRepo;
import com.store.merlindbatik.entity.Member;
import com.store.merlindbatik.entity.Transaction;
import com.store.merlindbatik.util.EmailUtil;

@RestController
@RequestMapping("transaction")
@CrossOrigin
public class TransactionController {
	
	@Autowired
	private TransactionRepo transactionRepo;
	
	@Autowired
	private MemberRepo memberRepo;
	
	@Autowired
	private EmailUtil emailUtil;
	
	private String uploadPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";
	
	
	@GetMapping
	public Iterable<Transaction> getAllTransaction(){
		return transactionRepo.findAll();
	}
	
	//History
	@GetMapping("/history/{userId}")
	public Iterable<Transaction> getTransactionByUser(@PathVariable int userId){
		Iterable<Transaction> getTransactionUser = transactionRepo.getTransactionUser(userId);
		return getTransactionUser;
	}
	
	
	//CheckOut
	@PostMapping("/addTransaction/{memberId}")
	public Transaction addTransaction(@RequestBody Transaction transaction, @PathVariable int memberId) {
		Member findUser = memberRepo.findById(memberId).get();
		transaction.setMember(findUser);
		return transactionRepo.save(transaction);
	}
	
	//Pembayaran / transaksi checkout
	@GetMapping("/uploadTransaction/{transactionId}")
	public Iterable<Transaction> getTransaction(@PathVariable int transactionId) {
		return transactionRepo.getTransactionByTransactionId(transactionId);
	}
	
	//Upload bukti Transfer
	@PutMapping("/uploadTransaction/{transactionId}")
	public String uploadBuktiTrf(@PathVariable int transactionId, @RequestParam("file") MultipartFile file) throws JsonMappingException, JsonProcessingException {
		Transaction findTransaction = transactionRepo.findById(transactionId).get();
		
		Date date = new Date();
		String fileExtension = file.getContentType().split("/")[1];
		String newFileName = "IMG-TRX-" + findTransaction.getMember().getUsername() + date.getTime() + "." + fileExtension;
		String fileName = StringUtils.cleanPath(newFileName);
		Path path = Paths.get(StringUtils.cleanPath(uploadPath) + fileName);
		try {
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/transaction/download/").path(fileName).toUriString();
		findTransaction.setBuktiTransfer(fileDownloadUri);
		findTransaction.setStatus("Pending");
		transactionRepo.save(findTransaction);
		return fileDownloadUri;
	}
	
	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Object> downloadFile(@PathVariable String fileName){
		Path path = Paths.get(uploadPath + fileName);
		Resource resource = null;
		
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		System.out.println("DOWNLOAD");
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ resource.getFilename()+ "\"").body(resource);
	}
	
	//Admin Reject
	@PutMapping("/reject/{transactionId}")
	public Transaction rejectTransaction(@PathVariable int transactionId) {
		Transaction findTransaction = transactionRepo.findById(transactionId).get();
		findTransaction.setStatus("Harap Kirim Ulang Bukti Pembayaran");
		findTransaction.setBuktiTransfer(null);
		return transactionRepo.save(findTransaction);
	}
	
	//Admin Accept
	String message ="";
	@PutMapping("/accept/{transactionId}")
	public Transaction acceptTransactions(@PathVariable int transactionId) {
		Transaction findTransactions = transactionRepo.findById(transactionId).get();
		findTransactions.setStatus("Accepted");
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		findTransactions.getTransactionDetails().forEach(val ->{
			if (val.getSize().equals("S")) {
				val.getProducts().setStockSizeS(val.getProducts().getStockSizeS() - val.getQuantity());
				val.getProducts().setSold(val.getProducts().getSold() + val.getQuantity());
			}
			else if (val.getSize().equals("M")) {
				val.getProducts().setStockSizeM(val.getProducts().getStockSizeM() - val.getQuantity());
				val.getProducts().setSold(val.getProducts().getSold() + val.getQuantity());
			}
			else if (val.getSize().equals("L")) {
				val.getProducts().setStockSizeL(val.getProducts().getStockSizeL() - val.getQuantity());
				val.getProducts().setSold(val.getProducts().getSold() + val.getQuantity());
			}
		});
		findTransactions.setTanggalAcc(formatter.format(date));
		transactionRepo.save(findTransactions);
		
		
		message = "<h1>Selamat! Pembelian Anda Berhasil</h1>\n";
		message += "<h3> Akun dengan username " + findTransactions.getMember().getUsername() + " telah bertransaksi seperti berikut : </h3>\n";
		message += "<h4> Tanggal Beli : " + findTransactions.getTanggalBeli() + "</h4> \n";
		message += "<h4> Tanggal Acc : " + findTransactions.getTanggalAcc() + "</h4> \n";
		message += "<h4> Jasa Pengiriman: " + findTransactions.getJasaPengiriman() + "</h4> \n";
		message += "<h4> Price : Rp." + findTransactions.getTotalPrice() + "</h4> \n";	
		message += "<h4> Dengan Detail Seperti Berikut : </h4> \n";
		findTransactions.getTransactionDetails().forEach(val -> {
			message += "<h5>"+ val.getProducts().getProductName() +" dengan harga Rp."+ val.getPrice()+", sebanyak "
					+ val.getQuantity()+ " pcs. SubTotal : Rp."+ val.getTotalPriceProduct() + ". (Product)"+ "</h5> \n";
		});
		String judulPesan = "INVOICE " + findTransactions.getMember().getUsername() + " " + findTransactions.getTanggalAcc();
		emailUtil.sendEmail(findTransactions.getMember().getEmail(), judulPesan, message);
		
		return findTransactions;
		
	}

}
