package com.ey.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ey.entity.Alert;


@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
	
	List<Alert> findByDueDateAndActiveTrue(LocalDate date);
	
	List<Alert> findByActiveTrue();

    List<Alert> findByActiveTrueAndDueDateLessThanEqual(LocalDate date);


}
