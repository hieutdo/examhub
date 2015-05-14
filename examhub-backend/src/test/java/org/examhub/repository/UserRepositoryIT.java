package org.examhub.repository;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.examhub.ExamHubApplication;
import org.examhub.domain.Role;
import org.examhub.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Hieu Do
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExamHubApplication.class)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class
})
@DatabaseSetup("users.xml")
@SuppressWarnings("unchecked")
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Test
    public void findOneByUsername_GivenAnUserHasUsernameXInDatabase_WhenXIsUsedToQuery_ThenReturnAnUserObjectThatHasUsernameX() throws Exception {
        User user = userRepository.findOneByUsername("nrichards0");

        assertThat(user, notNullValue());
        assertThat(user, allOf(
            hasProperty("id", is(1L)),
            hasProperty("username", is("nrichards0")),
            hasProperty("password", is("7KawtUguX")),
            hasProperty("firstName", is("Nancy")),
            hasProperty("lastName", is("Richards")),
            hasProperty("email", is("nrichards0@hc360.com")),
            hasProperty("activated", is(true)),
            hasProperty("roles", containsInAnyOrder(new Role("ROLE_ADMIN"), new Role("ROLE_USER")))
        ));
    }

    @Test
    public void findAll_GivenFiveUsersInDatabase_ThenReturnAListOfFiveUserObjects() throws Exception {
        List<User> users = userRepository.findAll();

        assertThat(users.size(), is(5));
        assertThat(users, contains(
            allOf(
                hasProperty("id", is(1L)),
                hasProperty("username", is("nrichards0"))
            ),
            allOf(
                hasProperty("id", is(2L)),
                hasProperty("username", is("vwashington1"))
            ),
            allOf(
                hasProperty("id", is(3L)),
                hasProperty("username", is("apeters2"))
            ),
            allOf(
                hasProperty("id", is(4L)),
                hasProperty("username", is("nmartin3"))
            ),
            allOf(
                hasProperty("id", is(5L)),
                hasProperty("username", is("tcarr4"))
            )
        ));
    }

    @Test
    @ExpectedDatabase(value = "users-after-delete.xml", table = User.TABLE_NAME)
    public void delete_GivenFiveUsersInDatabase_WhenAnUserHasIdXIsDeleted_ThenThereIsNoUserHasIdXInDatabase() throws Exception {
        long userId = 5L;
        userRepository.delete(userId);
        assertThat(userRepository.findOne(userId), is(nullValue()));
    }
}
