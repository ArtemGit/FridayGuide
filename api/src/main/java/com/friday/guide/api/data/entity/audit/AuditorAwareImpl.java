package com.friday.guide.api.data.entity.audit;


import com.friday.guide.api.service.security.user._User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<IdentifiedNamedEntity> {

    @Override
    public IdentifiedNamedEntity getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        IdentifiedNamedEntity identifiedNamedEntity = null;

        if (authentication.getPrincipal() instanceof _User) {
            _User portalUser = ((_User) authentication.getPrincipal());
            identifiedNamedEntity = new IdentifiedNamedEntity(portalUser.getAccountId(), portalUser.getLogin());
        }
        return identifiedNamedEntity;
    }

}