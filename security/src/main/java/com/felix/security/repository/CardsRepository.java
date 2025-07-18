package com.felix.security.repository;

import com.felix.security.entity.Accounts;
import com.felix.security.entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsRepository extends JpaRepository<Cards, Long> {

  List<Cards> findByCustomerId(long customerId);

}
