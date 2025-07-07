package com.felix.security.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationEvents {

  @EventListener
  public void onSuccess(AuthenticationSuccessEvent successEvent) {
    // In real case scenario, we might interact with the database to updates the recent logged in information.
    log.info("Login successful for the user : {}", successEvent.getAuthentication().getName());
  }

  @EventListener
  public void onFailure(AbstractAuthenticationFailureEvent failureEvent) {
    // In real case scenario, the implementation is not this simple.
    // We might send the email for more details or anything else based on our business requirement.
    log.error("Login failed for the user : {} due to : {}", failureEvent.getAuthentication().getName(),
      failureEvent.getException().getMessage());
  }

}
