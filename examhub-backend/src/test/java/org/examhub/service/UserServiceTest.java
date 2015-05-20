package org.examhub.service;

import org.examhub.domain.Authority;
import org.examhub.domain.UserAccount;
import org.examhub.repository.AuthorityRepository;
import org.examhub.repository.UserAccountRepository;
import org.examhub.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Hieu Do
 */
public class UserServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userAccountRepository, authorityRepository, passwordEncoder);
    }

    @Test
    public void testCreateNewUser() throws Exception {
        String username = "newUser";
        String password = "123456";
        String passwordHash = "654321";
        String email = "abc@mail.com";
        Authority userRole = new Authority("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(userRole);
        UserAccount expectedUser = new UserAccount(username, passwordHash, email, authorities);

        when(passwordEncoder.encode(password)).thenReturn(passwordHash);
        when(authorityRepository.findOne("ROLE_USER")).thenReturn(userRole);
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(expectedUser);

        UserAccount actualUser = userService.createNewUser(username, password, email);

        assertThat(actualUser, allOf(
            notNullValue(),
            hasProperty("username", is(username)),
            hasProperty("password", is(passwordHash)),
            hasProperty("email", is(email)),
            hasProperty("accountNonExpired", is(true)),
            hasProperty("accountNonLocked", is(true)),
            hasProperty("credentialsNonExpired", is(true)),
            hasProperty("enabled", is(true)),
            hasProperty("activated", is(false))
        ));
    }
}
