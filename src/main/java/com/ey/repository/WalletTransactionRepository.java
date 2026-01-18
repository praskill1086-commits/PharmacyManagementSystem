package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ey.entity.WalletTransaction;


@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
	List<WalletTransaction> findByWalletId(Long walletId);

	


}
