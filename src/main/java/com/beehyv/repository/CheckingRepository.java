package com.beehyv.repository;

import com.beehyv.domain.Checking;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Checking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckingRepository extends JpaRepository<Checking, Long> {

}
