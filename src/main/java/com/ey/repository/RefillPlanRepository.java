package com.ey.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ey.entity.RefillPlan;


@Repository
public interface RefillPlanRepository extends JpaRepository<RefillPlan, Long> {
	List<RefillPlan> findByNextRefillDateAndActiveTrue(LocalDate date);
	
	 List<RefillPlan> findByActiveTrue();

	 List<RefillPlan> findByActiveTrueAndNextRefillDateLessThanEqual(LocalDate date);


}
