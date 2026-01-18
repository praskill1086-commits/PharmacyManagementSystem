package com.ey.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ey.entity.Medicine;


@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
	
	List<Medicine> findByActiveTrue();

	List<Medicine> findByQuantityLessThan(int threshold);

	List<Medicine> findByExpiryDateBetween(LocalDate start, LocalDate end);

	List<Medicine> findByExpiryDateBefore(LocalDate date);

}
