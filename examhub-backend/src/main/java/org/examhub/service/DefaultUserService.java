package org.examhub.service;

import org.examhub.domain.UserAccount;
import org.examhub.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Hieu Do
 */
@Service
@Transactional
public class DefaultUserService implements UserService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccount getUser(String username) {
        return userAccountRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccount getUserWithAuthorities(String username) {
        UserAccount userAccount = userAccountRepository.findByUsernameIgnoreCase(username);
        userAccount.getAuthorities().size();
        return userAccount;
    }
}
