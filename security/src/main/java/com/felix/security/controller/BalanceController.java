package com.felix.security.controller;

import com.felix.security.entity.AccountTransactions;
import com.felix.security.repository.AccountTransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BalanceController {

  private final AccountTransactionsRepository accountTransactionsRepository;

  @GetMapping("/myBalance")
  public List<AccountTransactions> getBalanceDetails(@RequestParam long id) {
    List<AccountTransactions> accountTransactions = accountTransactionsRepository.
      findByCustomerIdOrderByTransactionDtDesc(id);
    if (accountTransactions != null) {
      return accountTransactions;
    } else {
      return null;
    }
  }
}
