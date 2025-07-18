package com.felix.security.controller;

import com.felix.security.entity.Loans;
import com.felix.security.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoanController {

  private final LoanRepository loanRepository;

  @GetMapping("/myLoans")
  public List<Loans> getLoanDetails(@RequestParam long id) {
    List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(id);
    if (loans != null) {
      return loans;
    } else {
      return null;
    }
  }

}
