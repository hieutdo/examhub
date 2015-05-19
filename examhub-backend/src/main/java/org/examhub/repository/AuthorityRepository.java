package org.examhub.repository;

import org.examhub.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hieu Do
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
