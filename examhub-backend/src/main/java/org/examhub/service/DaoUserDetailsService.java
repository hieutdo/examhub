package org.examhub.service;

import org.examhub.domain.UserAccount;
import org.examhub.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hieu Do
 */
@Service
public class DaoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsernameIgnoreCase(username);
        if (userAccount == null) {
            throw new UsernameNotFoundException("Username " + username + " was not found in database");
        }
        // eagerly load the Authority collection
        userAccount.getAuthorities().size();
        return userAccount;
    }
}
