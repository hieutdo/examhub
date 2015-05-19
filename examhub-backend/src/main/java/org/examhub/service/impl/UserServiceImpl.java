package org.examhub.service.impl;

import org.examhub.domain.UserAccount;
import org.examhub.repository.UserAccountRepository;
import org.examhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Hieu Do
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

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
}
