package com.felix.security.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * AuditAwareImpl returns the authenticated user's username for auditing.
 * Rejects unauthenticated or anonymous actions by returning empty.
 */
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Reject if no authentication or if it's not an authenticated user
    if (
      authentication == null ||
        !authentication.isAuthenticated() ||
        "anonymousUser".equals(authentication.getPrincipal())
    ) {
      return Optional.empty(); // will skip setting @CreatedBy/@ModifiedBy
    }

    return Optional.of(authentication.getName());
  }
}
