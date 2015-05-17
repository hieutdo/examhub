package org.examhub.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Hieu Do
 */
public enum Role implements GrantedAuthority {
    ADMIN, USER, ANONYMOUS;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
