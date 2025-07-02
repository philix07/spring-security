package com.felix.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

  @GetMapping("/loans")
  public String getLoanDetails() {
    return "Loan Detail From DB";
  }


}
