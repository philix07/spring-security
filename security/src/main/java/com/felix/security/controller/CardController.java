package com.felix.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

  @GetMapping("/cards")
  public String getCardDetails() {
    return "Card Detail From DB";
  }

}
