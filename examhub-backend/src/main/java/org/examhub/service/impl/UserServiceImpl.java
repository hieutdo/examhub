package org.examhub.service.impl;

import org.examhub.domain.Authority;
import org.examhub.domain.UserAccount;
import org.examhub.repository.AuthorityRepository;
import org.examhub.repository.UserAccountRepository;
import org.examhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hieu Do
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userAccountRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserAccountRepository userAccountRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccount getUserByUsername(String username) {
        return userAccountRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAccount getUserByEmail(String email) {
        return userAccountRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public UserAccount createNewUser(String username, String password, String email) {
        String passwordHash = passwordEncoder.encode(password);

        // assign ROLE_USER to new user
        Authority roleUser = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(roleUser);

        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(username);
        userAccount.setPassword(passwordHash);
        userAccount.setEmail(email);
        userAccount.setAuthorities(authorities);
        // default values
        userAccount.setAccountNonExpired(true);
        userAccount.setAccountNonLocked(true);
        userAccount.setCredentialsNonExpired(true);
        userAccount.setEnabled(true);
        userAccount.setActivated(false);

        return userAccountRepository.save(userAccount);
    }
}
