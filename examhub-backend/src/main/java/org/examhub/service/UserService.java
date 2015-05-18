package org.examhub.service;

import org.examhub.domain.UserAccount;

import java.util.List;

/**
 * @author Hieu Do
 */
public interface UserService {
    List<UserAccount> getAllUsers();

    UserAccount getUser(String username);
}
