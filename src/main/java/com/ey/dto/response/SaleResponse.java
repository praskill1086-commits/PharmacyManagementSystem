package com.ey.dto.response;

import java.time.LocalDateTime;

public class SaleResponse {
    private Long saleId;
    private double subtotal;
    private double discount;
    private double walletUsed;
    private double finalAmount;
    private double cashbackEarned;
    private LocalDateTime date;
	public Long getSaleId() {
		return saleId;
	}
	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getWalletUsed() {
		return walletUsed;
	}
	public void setWalletUsed(double walletUsed) {
		this.walletUsed = walletUsed;
	}
	public double getFinalAmount() {
		return finalAmount;
	}
	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}
	public double getCashbackEarned() {
		return cashbackEarned;
	}
	public void setCashbackEarned(double cashbackEarned) {
		this.cashbackEarned = cashbackEarned;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
    
    
    
}
