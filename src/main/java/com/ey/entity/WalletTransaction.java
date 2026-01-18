package com.ey.entity;

import java.time.LocalDateTime;

import com.ey.enums.WalletTransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Wallet wallet;

    private double amount;   // positive = credit, negative = debit
    
    @Enumerated(EnumType.STRING)
    private WalletTransactionType type;

    @NotBlank
    private String reason;

    private LocalDateTime date;

    public WalletTransaction() {}

    public WalletTransaction(Wallet wallet, double amount, String reason) {
        this.wallet = wallet;
        this.amount = amount;
        this.reason = reason;
        this.date = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public void setType(WalletTransactionType cashback) {
		this.type= cashback;
		
	}

	public WalletTransactionType getType() {
		return type;
	}

	
    
    
}
