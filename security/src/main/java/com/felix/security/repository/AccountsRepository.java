package com.felix.security.repository;

import com.felix.security.entity.AccountTransactions;
import com.felix.security.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

  Accounts findByCustomerId(long customerId);

}
