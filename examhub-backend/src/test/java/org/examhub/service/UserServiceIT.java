package org.examhub.service;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.examhub.ExamHubApplication;
import org.examhub.config.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Hieu Do
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExamHubApplication.class)
@WebAppConfiguration
@TestExecutionListeners(
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
    listeners = {
        WithSecurityContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
    })
public class UserServiceIT {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void getAllUsers_ShouldReturn401ErrorWhenUserIsNotAuthenticated() throws Exception {
        this.mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user")
    public void getAllUsers_ShouldReturn403ErrorWhenUserDoesNotHaveAdminRole() throws Exception {
        this.mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("admin")
    @DatabaseSetup("users.xml")
    @ExpectedDatabase(value = "users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void getAllUsers_ShouldReturnAListOfThreeUserAccountsWhenUserHasAdminRole() throws Exception {
        this.mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(Constants.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].username", is("system")))
            .andExpect(jsonPath("$[0].authorities[*].name", containsInAnyOrder("ROLE_ADMIN", "ROLE_USER")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].username", is("admin")))
            .andExpect(jsonPath("$[1].authorities[*].name", containsInAnyOrder("ROLE_ADMIN", "ROLE_USER")))
            .andExpect(jsonPath("$[2].id", is(3)))
            .andExpect(jsonPath("$[2].username", is("user")))
            .andExpect(jsonPath("$[2].authorities[*].name", containsInAnyOrder("ROLE_USER")));
    }
}
