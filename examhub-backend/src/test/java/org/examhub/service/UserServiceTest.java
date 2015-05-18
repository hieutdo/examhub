package org.examhub.service;

import org.examhub.ExamHubApplication;
import org.examhub.domain.UserAccount;
import org.examhub.repository.UserAccountRepository;
import org.examhub.utils.AspectUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

/**
 * @author Hieu Do
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExamHubApplication.class)
@TestExecutionListeners({
    ServletTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    WithSecurityContextTestExecutionListener.class
})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(userAccountRepository.findAll()).thenReturn(Arrays.asList(new UserAccount()));

        ReflectionTestUtils.setField(AspectUtils.unwrapProxy(userService), "userAccountRepository", userAccountRepository);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void getAllUsers_ShouldThrowExceptionWhenUserIsNotAuthenticated() throws Exception {
        userService.getAllUsers();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsersWithUserAdmin() throws Exception {
        List<UserAccount> userAccounts = userService.getAllUsers();
        Assert.assertThat(userAccounts, hasSize(1));
    }


}
