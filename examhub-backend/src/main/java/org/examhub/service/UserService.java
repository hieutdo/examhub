package org.examhub.service;

import org.examhub.domain.UserAccount;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * @author Hieu Do
 */
public interface UserService {
    @PreAuthorize("hasRole('ADMIN')")
    List<UserAccount> getAllUsers();

    UserAccount getUser(String username);
}
