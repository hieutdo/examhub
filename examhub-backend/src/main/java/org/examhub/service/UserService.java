package org.examhub.service;

import org.examhub.domain.UserAccount;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * @author Hieu Do
 */
public interface UserService {
    @PreAuthorize("hasRole('ADMIN')")
    List<UserAccount> getAllUsers();

//    @PostAuthorize("(isAuthenticated() and returnObject.username == principal.username) or hasRole('ADMIN')")
    UserAccount getUserByUsername(String username);

    UserAccount getUserByEmail(String email);

    UserAccount createNewUser(String username, String password, String email);
}
