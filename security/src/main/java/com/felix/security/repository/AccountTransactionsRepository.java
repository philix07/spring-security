package com.felix.security.repository;

import com.felix.security.entity.AccountTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTransactionsRepository extends JpaRepository<AccountTransactions, String> {

  List<AccountTransactions> findByCustomerIdOrderByTransactionDtDesc(long customerId);

}
