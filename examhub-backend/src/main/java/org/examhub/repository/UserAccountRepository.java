package org.examhub.repository;

import org.examhub.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hieu Do
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsernameIgnoreCase(String username);
}
