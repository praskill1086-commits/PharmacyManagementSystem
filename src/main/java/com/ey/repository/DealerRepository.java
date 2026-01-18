package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ey.entity.Dealer;


@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {
	List<Dealer> findByActiveTrue();


}
