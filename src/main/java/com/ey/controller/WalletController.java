package com.ey.controller;

import com.ey.dto.response.*;
import com.ey.service.WalletService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    @GetMapping("/{customerId}")
    public WalletResponse getWallet(@PathVariable Long customerId) {
        return service.getWalletByCustomer(customerId);
    }

    @GetMapping("/{customerId}/transactions")
    public List<WalletTransactionResponse> getTransactions(
            @PathVariable Long customerId
    ) {
        return service.getTransactions(customerId);
    }
}
