package com.ey.service;

import com.ey.dto.response.WalletResponse;
import com.ey.dto.response.WalletTransactionResponse;

import java.util.List;

public interface WalletService {

    WalletResponse getWalletByCustomer(Long customerId);

    List<WalletTransactionResponse> getTransactions(Long customerId);
}
