package com.ey.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.config.CurrentUserUtil;
import com.ey.dto.request.CreateSaleRequest;
import com.ey.dto.request.SaleItemRequest;
import com.ey.dto.response.SaleResponse;
import com.ey.dto.response.SalesSummaryResponse;
import com.ey.entity.Customer;
import com.ey.entity.Medicine;
import com.ey.entity.Offer;
import com.ey.entity.Sale;
import com.ey.entity.SaleItem;
import com.ey.entity.Wallet;
import com.ey.entity.WalletTransaction;
import com.ey.enums.WalletTransactionType;
import com.ey.exception.SaleOperationException;
import com.ey.repository.CustomerRepository;
import com.ey.repository.MedicineRepository;
import com.ey.repository.OfferRepository;
import com.ey.repository.SaleItemRepository;
import com.ey.repository.SaleRepository;
import com.ey.repository.WalletRepository;
import com.ey.repository.WalletTransactionRepository;
import com.ey.service.SaleService;

@Service
public class SaleServiceImpl implements SaleService {

	private static final Logger logger =
            LoggerFactory.getLogger(SaleServiceImpl.class);

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private SaleRepository saleRepo;

    @Autowired
    private SaleItemRepository saleItemRepo;

    @Autowired
    private WalletRepository walletRepo;

    @Autowired
    private WalletTransactionRepository walletTxnRepo;

    @Autowired
    private OfferRepository offerRepo;

    @Autowired
    private CurrentUserUtil currentUser;

    @Override
    public SaleResponse createSale(CreateSaleRequest request) {

        logger.info("Starting sale for customer {}", request.getCustomerId());

        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new SaleOperationException("Customer not found"));

        double subtotal = 0;

        // Validate stock & expiry
        for (SaleItemRequest item : request.getItems()) {

            Medicine med = medicineRepo.findById(item.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found"));

            if (!med.isActive()) {
                throw new RuntimeException("Medicine disabled: " + med.getName());
            }

            if (med.getExpiryDate().isBefore(LocalDate.now())) {
                throw new RuntimeException("Medicine expired: " + med.getName());
            }

            if (med.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + med.getName());
            }

            subtotal += med.getPrice() * item.getQuantity();
        }

        double discount = 0;
        double walletUsed = 0;

        Offer offer = offerRepo
                .findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        LocalDate.now(), LocalDate.now())
                .orElse(null);

        // Wallet OR Offer
        if (request.isUseWallet()) {

            Wallet wallet = walletRepo.findByCustomerId(customer.getId())
                    .orElseThrow(() -> new RuntimeException("Wallet not found"));

            if (wallet.getBalance() <= 0) {
                throw new RuntimeException("Wallet balance is zero");
            }

            walletUsed = Math.min(wallet.getBalance(), subtotal);
            wallet.setBalance(wallet.getBalance() - walletUsed);
            wallet.setUpdatedAt(LocalDateTime.now());
            wallet.setUpdatedBy(currentUser.getCurrentUser());
            walletRepo.save(wallet);

            WalletTransaction txn = new WalletTransaction();
            txn.setWallet(wallet);
            txn.setAmount(-walletUsed);
            txn.setType(WalletTransactionType.REDEMPTION);
            txn.setReason("Wallet redemption during sale");
            txn.setDate(LocalDateTime.now());
            walletTxnRepo.save(txn);

        } else if (offer != null) {
            discount = subtotal * offer.getDiscountPercent() / 100;
        }

        double finalAmount = subtotal - discount - walletUsed;

        // Create Sale
        Sale sale = new Sale();
        sale.setSaleDate(LocalDateTime.now());
        sale.setTotalAmount(finalAmount);
        sale.setCustomer(customer);
        sale.setStaff(currentUser.getCurrentUser());
        sale.setActive(true);
        sale.setCreatedAt(LocalDateTime.now());
        sale.setUpdatedAt(LocalDateTime.now());
        sale.setUpdatedBy(currentUser.getCurrentUser());
        sale = saleRepo.save(sale);

        // Reduce stock & save sale items
        for (SaleItemRequest item : request.getItems()) {

            Medicine med = medicineRepo.findById(item.getMedicineId()).orElseThrow();
            med.setQuantity(med.getQuantity() - item.getQuantity());
            medicineRepo.save(med);

            SaleItem si = new SaleItem();
            si.setSale(sale);
            si.setMedicine(med);
            si.setQuantity(item.getQuantity());
            si.setPrice(med.getPrice());
            saleItemRepo.save(si);
        }

        // Cashback (5%)
        double cashback = finalAmount * 0.05;

        Wallet wallet = walletRepo.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Wallet w = new Wallet();
                    w.setCustomer(customer);
                    w.setBalance(0);
                    w.setCreatedAt(LocalDateTime.now());
                    w.setUpdatedAt(LocalDateTime.now());
                    w.setUpdatedBy(currentUser.getCurrentUser());
                    return walletRepo.save(w);
                });

        wallet.setBalance(wallet.getBalance() + cashback);
        wallet.setUpdatedAt(LocalDateTime.now());
        wallet.setUpdatedBy(currentUser.getCurrentUser());
        walletRepo.save(wallet);

        WalletTransaction cashbackTxn = new WalletTransaction();
        cashbackTxn.setWallet(wallet);
        cashbackTxn.setAmount(cashback);
        cashbackTxn.setType(WalletTransactionType.CASHBACK);
        cashbackTxn.setReason("Cashback earned on sale");
        cashbackTxn.setDate(LocalDateTime.now());
        walletTxnRepo.save(cashbackTxn);

        logger.info("Sale completed with id {}", sale.getId());

        SaleResponse resp = new SaleResponse();
        resp.setSaleId(sale.getId());
        resp.setSubtotal(subtotal);
        resp.setDiscount(discount);
        resp.setWalletUsed(walletUsed);
        resp.setFinalAmount(finalAmount);
        resp.setCashbackEarned(cashback);
        resp.setDate(sale.getSaleDate());

        return resp;
    }

    @Override
    public List<SaleResponse> getAllSales() {
        return saleRepo.findAll().stream().map(s -> {
            SaleResponse r = new SaleResponse();
            r.setSaleId(s.getId());
            r.setFinalAmount(s.getTotalAmount());
            r.setDate(s.getSaleDate());
            return r;
        }).toList();
    }

    @Override
    public SaleResponse getSaleById(Long id) {
        Sale s = saleRepo.findById(id).orElseThrow();
        SaleResponse r = new SaleResponse();
        r.setSaleId(s.getId());
        r.setFinalAmount(s.getTotalAmount());
        r.setDate(s.getSaleDate());
        return r;
    }

    @Override
    public List<SaleResponse> getSalesBetween(LocalDateTime from, LocalDateTime to) {
        return saleRepo.findAll().stream()
                .filter(s -> !s.getSaleDate().isBefore(from) && s.getSaleDate().isBefore(to))
                .map(s -> {
                    SaleResponse r = new SaleResponse();
                    r.setSaleId(s.getId());
                    r.setFinalAmount(s.getTotalAmount());
                    r.setDate(s.getSaleDate());
                    return r;
                })
                .toList();
    }

	@Override
	public double getTotalBetween(LocalDateTime from, LocalDateTime to) {
		return saleRepo.findAll().stream()
	            .filter(s -> !s.getSaleDate().isBefore(from)
	                      && s.getSaleDate().isBefore(to))
	            .mapToDouble(Sale::getTotalAmount)
	            .sum();
	}
	
	@Override
	public SalesSummaryResponse getSalesSummary(LocalDate from, LocalDate to) {

	    LocalDateTime start = from.atStartOfDay();
	    LocalDateTime end = to.plusDays(1).atStartOfDay();

	    List<Sale> sales = saleRepo.findAll().stream()
	            .filter(s -> !s.getSaleDate().isBefore(start)
	                      && s.getSaleDate().isBefore(end))
	            .toList();

	    double total = sales.stream()
	            .mapToDouble(Sale::getTotalAmount)
	            .sum();

	    SalesSummaryResponse resp = new SalesSummaryResponse();
	    resp.setFromDate(from);
	    resp.setToDate(to);
	    resp.setNumberOfSales(sales.size());
	    resp.setTotalRevenue(total);

	    return resp;
	}


	

}
