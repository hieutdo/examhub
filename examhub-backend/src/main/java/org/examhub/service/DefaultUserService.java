package org.examhub.service;

import org.examhub.domain.Authority;
import org.examhub.domain.UserAccount;
import org.examhub.exception.EmailAlreadyExistsException;
import org.examhub.exception.UsernameAlreadyExistsException;
import org.examhub.repository.AuthorityRepository;
import org.examhub.repository.UserAccountRepository;
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
public class DefaultUserService implements UserService {

    private final UserAccountRepository userAccountRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserService(UserAccountRepository userAccountRepository,
                              AuthorityRepository authorityRepository,
                              PasswordEncoder passwordEncoder) {
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
        // check if username and email already exist in database
        if (getUserByUsername(username) != null) {
            throw new UsernameAlreadyExistsException(username);
        }
        if (getUserByEmail(email) != null) {
            throw new EmailAlreadyExistsException(email);
        }
        // generate password hash
        String passwordHash = passwordEncoder.encode(password);
        // assign ROLE_USER to new user
        Authority roleUser = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(roleUser);

        return userAccountRepository.save(new UserAccount(username, passwordHash, email, authorities));
    }
}
