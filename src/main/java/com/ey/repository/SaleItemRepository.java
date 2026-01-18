package com.ey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ey.entity.SaleItem;


@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
	


}
