package com.ey.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ey.entity.Customer;
import com.ey.entity.Offer;


@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
	Optional<Offer> findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate today1, LocalDate today2);

	


}
