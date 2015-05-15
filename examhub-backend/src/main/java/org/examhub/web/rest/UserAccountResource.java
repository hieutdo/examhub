package org.examhub.web.rest;

import org.examhub.domain.UserAccount;
import org.examhub.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Hieu Do
 */
@RestController
@RequestMapping("/api/v1")
public class UserAccountResource {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountResource(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<UserAccount> findAll() {
        return userAccountRepository.findAll();
    }
}
