package com.ey.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ey.entity.Sale;


@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
	List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

	


}
