package org.burgas.portfolioservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.exception.IdentityNotFoundException;
import org.burgas.portfolioservice.message.IdentityMessages;
import org.burgas.portfolioservice.repository.IdentityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IdentityRepository identityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.identityRepository.findIdentityByEmail(username)
                .orElseThrow(
                        () -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage())
                );
    }
}
