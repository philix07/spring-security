package com.felix.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticeController {

  @GetMapping("/notices")
  public String getNotices() {
    return "Get Notices Detail From DB";
  }

}
