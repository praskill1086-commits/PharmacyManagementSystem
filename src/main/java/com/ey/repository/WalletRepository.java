package com.ey.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ey.entity.Wallet;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
	Optional<Wallet> findByCustomerId(Long customerId);


}
