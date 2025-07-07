package com.felix.security.repository;

import com.felix.security.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loans, Long> {

  List<Loans> findByCustomerIdOrderByStartDtDesc(long customerId);

}
