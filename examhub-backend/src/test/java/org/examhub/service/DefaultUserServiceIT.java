package org.examhub.service;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.examhub.ExamHubApplication;
import org.examhub.config.Constants;
import org.examhub.domain.UserAccount;
import org.examhub.repository.UserAccountRepository;
import org.examhub.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class DefaultUserServiceIT {

    @Autowired
    private UserAccountRepository userAccountRepository;

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

    @Test
    public void getUser_ShouldReturn401ErrorWhenUserIsNotAuthenticated() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/user"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user")
    @DatabaseSetup("users.xml")
    @ExpectedDatabase(value = "users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void getUser_ShouldAnUserAccountWhenUserIsAccessingHisOwnAccount() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/user"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(Constants.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id", is(3)))
            .andExpect(jsonPath("$.username", is("user")))
            .andExpect(jsonPath("$.authorities[*].name", containsInAnyOrder("ROLE_USER")));
    }

    @Test
    @WithUserDetails("admin")
    @DatabaseSetup("users.xml")
    @ExpectedDatabase(value = "users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void getUser_ShouldAnUserAccountWhenUserIsAccessingSomeoneElseAccountButHasRoleAdmin() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/user"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(Constants.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.id", is(3)))
            .andExpect(jsonPath("$.username", is("user")))
            .andExpect(jsonPath("$.authorities[*].name", containsInAnyOrder("ROLE_USER")));
    }

    @Test
    @DatabaseSetup("users.xml")
    @ExpectedDatabase(value = "users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void userAuthentication_ShouldReturnOkIfUserProvidesCorrectCredentials() throws Exception {
        this.mockMvc.perform(post("/api/v1/authenticate")
            .with(csrf().asHeader())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "user")
            .param("password", "user"))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    @DatabaseSetup("users.xml")
    @ExpectedDatabase(value = "users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void userAuthentication_ShouldReturn401ErrorIfUserProvidesWrongCredentials() throws Exception {
        this.mockMvc.perform(post("/api/v1/authenticate")
            .with(csrf().asHeader())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "user")
            .param("password", "foo"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DatabaseSetup("users.xml")
    @ExpectedDatabase(value = "users.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void userAuthentication_ShouldReturn401ErrorIfGivenUsernameNotFound() throws Exception {
        this.mockMvc.perform(post("/api/v1/authenticate")
            .with(csrf().asHeader())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "foo")
            .param("password", "bar"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DatabaseSetup("users.xml")
    public void userRegistration_ShouldCreateANewUserWhenNoUsernameOrEmailExist() throws Exception {
        UserAccount newUser = new UserAccount();
        newUser.setUsername("newUser");
        newUser.setPassword("123456");
        newUser.setEmail("newUser@mail.com");

        this.mockMvc.perform(post("/api/v1/register")
            .with(csrf().asHeader())
            .contentType(Constants.APPLICATION_JSON_UTF8)
            .content(JsonUtils.convertObjectToJsonBytes(newUser)))
            .andExpect(status().isCreated())
            .andExpect(content().string(""));

        assertThat(userAccountRepository.findByUsernameIgnoreCase("newUser"), allOf(
            hasProperty("id", is(4L)),
            hasProperty("username", is("newUser")),
            hasProperty("email", is("newUser@mail.com"))
        ));
    }
}
