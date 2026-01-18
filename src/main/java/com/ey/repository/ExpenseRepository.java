package com.ey.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ey.entity.Expense;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	 List<Expense> findByActiveTrue();
	 List<Expense> findByActiveTrueAndDateBetween(LocalDate start, LocalDate end);

}
