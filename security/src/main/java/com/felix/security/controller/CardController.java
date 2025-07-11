package com.felix.security.controller;

import com.felix.security.entity.Cards;
import com.felix.security.repository.CardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardController {

  private final CardsRepository cardsRepository;

  @GetMapping("/myCards")
  public List<Cards> getCardDetails(@RequestParam long id) {
    List<Cards> cards = cardsRepository.findByCustomerId(id);
    if (cards != null) {
      return cards;
    } else {
      return null;
    }
  }

}
