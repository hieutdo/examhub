package org.examhub.service.impl;

import org.examhub.domain.UserAccount;
import org.examhub.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Hieu Do
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsernameIgnoreCase(username);
        if (userAccount == null) {
            throw new UsernameNotFoundException("Username " + username + " was not found in database");
        }
        return userAccount;
    }
}
