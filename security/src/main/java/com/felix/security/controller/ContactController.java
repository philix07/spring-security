package com.felix.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

  @GetMapping("/contacts")
  public String saveContactInquiryDetails() {
    return "Inquiry details are saved to DB";
  }


}
