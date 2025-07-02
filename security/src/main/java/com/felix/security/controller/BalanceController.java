package com.felix.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

  @GetMapping("/balances")
  public String getBalanceDetail() {
    return "Get Balance Detail From DB";
  }

}
