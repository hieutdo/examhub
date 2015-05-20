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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;
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
        final String username = "newUser";
        final String password = "123456";
        final String passwordHash = "654321";
        final String email = "abc@mail.com";
        final Authority roleUser = new Authority("ROLE_USER");

        when(passwordEncoder.encode(password)).thenReturn(passwordHash);
        when(authorityRepository.findOne("ROLE_USER")).thenReturn(roleUser);
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(i -> i.getArgumentAt(0, UserAccount.class));

        final UserAccount newUser = userService.createNewUser(username, password, email);

        assertThat(newUser, allOf(
            notNullValue(),
            hasProperty("username", is(username)),
            hasProperty("password", is(passwordHash)),
            hasProperty("email", is(email)),
            hasProperty("authorities", contains(roleUser)),
            hasProperty("accountNonExpired", is(true)),
            hasProperty("accountNonLocked", is(true)),
            hasProperty("credentialsNonExpired", is(true)),
            hasProperty("enabled", is(true)),
            hasProperty("activated", is(false))
        ));
    }
}
