package com.ey.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ey.dto.response.WalletResponse;
import com.ey.dto.response.WalletTransactionResponse;
import com.ey.entity.Wallet;
import com.ey.repository.WalletRepository;
import com.ey.repository.WalletTransactionRepository;
import com.ey.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepo;
    private final WalletTransactionRepository txnRepo;

    public WalletServiceImpl(WalletRepository walletRepo,
                             WalletTransactionRepository txnRepo) {
        this.walletRepo = walletRepo;
        this.txnRepo = txnRepo;
    }

    @Override
    public WalletResponse getWalletByCustomer(Long customerId) {

        Wallet wallet = walletRepo.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        WalletResponse resp = new WalletResponse();
        resp.setWalletId(wallet.getId());
        resp.setCustomerId(wallet.getCustomer().getId());
        resp.setBalance(wallet.getBalance());
        resp.setCreatedAt(wallet.getCreatedAt());
        resp.setUpdatedAt(wallet.getUpdatedAt());
        resp.setUpdatedBy(
                wallet.getUpdatedBy() != null ? wallet.getUpdatedBy().getUsername() : null
        );

        return resp;
    }

    @Override
    public List<WalletTransactionResponse> getTransactions(Long customerId) {

        Wallet wallet = walletRepo.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        return txnRepo.findByWalletId(wallet.getId()).stream()
                .map(txn -> {
                    WalletTransactionResponse r = new WalletTransactionResponse();
                    r.setAmount(txn.getAmount());
                    r.setType(txn.getType().name());
                    r.setReason(txn.getReason());
                    r.setDate(txn.getDate());
                    return r;
                })
                .toList();
    }
}
