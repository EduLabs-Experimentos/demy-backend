package com.nistra.demy.platform.iam.infrastructure.identity.springsecurity.services;

import com.nistra.demy.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.nistra.demy.platform.iam.infrastructure.identity.springsecurity.SpringSecurityCurrentUserProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CurrentUserProviderImpl implements SpringSecurityCurrentUserProvider {

    @Override
    public Optional<Long> getUserId() {
        return getPrincipal().map(UserDetailsImpl::getUserId);
    }

    @Override
    public Optional<String> getUsername() {
        return getPrincipal().map(UserDetailsImpl::getUsername);
    }

    @Override
    public Set<String> getRoles() {
        return SecurityContextHolder.getContext()
                .getAuthentication() != null
                ? SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                : Set.of();
    }

    @Override
    public Optional<Long> getTenantId() {
        return getPrincipal().map(UserDetailsImpl::getTenantId);
    }

    @Override
    public boolean isServiceAccount() {
        // TODO: Implement logic to determine if the current user is a service account
        return false;
    }

    private Optional<UserDetailsImpl> getPrincipal() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl principal)) {
            return Optional.empty();
        }
        return Optional.of(principal);
    }
}
