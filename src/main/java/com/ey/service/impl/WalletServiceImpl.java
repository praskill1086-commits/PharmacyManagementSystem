package com.ey.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ey.dto.response.WalletResponse;
import com.ey.dto.response.WalletTransactionResponse;
import com.ey.entity.Wallet;
import com.ey.exception.WalletNotFoundException;
import com.ey.repository.WalletRepository;
import com.ey.repository.WalletTransactionRepository;
import com.ey.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger logger =
            LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    private WalletRepository walletRepo;

    @Autowired
    private WalletTransactionRepository txnRepo;

    @Override
    public WalletResponse getWalletByCustomer(Long customerId) {

        logger.info("Fetching wallet for customerId={}", customerId);

        Wallet wallet = walletRepo.findByCustomerId(customerId)
                .orElseThrow(() -> {
                    logger.warn("Wallet not found for customerId={}", customerId);
                    return new WalletNotFoundException("Wallet not found for customer");
                });

        WalletResponse resp = new WalletResponse();
        resp.setWalletId(wallet.getId());
        resp.setCustomerId(wallet.getCustomer().getId());
        resp.setBalance(wallet.getBalance());
        resp.setCreatedAt(wallet.getCreatedAt());
        resp.setUpdatedAt(wallet.getUpdatedAt());
        resp.setUpdatedBy(
                wallet.getUpdatedBy() != null
                        ? wallet.getUpdatedBy().getUsername()
                        : null
        );

        logger.info("Wallet fetched successfully for customerId={}", customerId);
        return resp;
    }

    @Override
    public List<WalletTransactionResponse> getTransactions(Long customerId) {

        logger.info("Fetching wallet transactions for customerId={}", customerId);

        Wallet wallet = walletRepo.findByCustomerId(customerId)
                .orElseThrow(() -> {
                    logger.warn("Wallet not found while fetching transactions, customerId={}", customerId);
                    return new WalletNotFoundException("Wallet not found for customer");
                });

        List<WalletTransactionResponse> responses =
                txnRepo.findByWalletId(wallet.getId()).stream()
                        .map(txn -> {
                            WalletTransactionResponse r = new WalletTransactionResponse();
                            r.setAmount(txn.getAmount());
                            r.setType(txn.getType().name()); // ENUM → String is correct
                            r.setReason(txn.getReason());
                            r.setDate(txn.getDate());
                            return r;
                        })
                        .toList();

        logger.info("Fetched {} wallet transactions for customerId={}",
                responses.size(), customerId);

        return responses;
    }
}
