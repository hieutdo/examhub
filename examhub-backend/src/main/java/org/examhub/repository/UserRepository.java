package org.examhub.repository;

import org.examhub.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hieu Do
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByUsername(String username);
}
