package com.nistra.demy.platform.iam.infrastructure.authorization.sfs.services;

import com.nistra.demy.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.nistra.demy.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.nistra.demy.platform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmailAddress(new EmailAddress(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserDetailsImpl.build(user);
    }
}
