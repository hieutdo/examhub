package org.examhub.repository;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.examhub.domain.User;
import org.examhub.test.AbstractBaseIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Hieu Do
 */
@DatabaseSetup("users.xml")
public class UserRepositoryIT extends AbstractBaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findOneByUsername_ShouldReturnAnExistingUser() throws Exception {
        User user = userRepository.findOneByUsername("nrichards0");

        assertThat(user, notNullValue());
        assertThat(user, allOf(
            hasProperty("id", is(1L)),
            hasProperty("firstName", is("Nancy")),
            hasProperty("lastName", is("Richards")),
            hasProperty("email", is("nrichards0@hc360.com"))
        ));
    }

    @Test
    public void findAll_FiveUsersFound_ShouldReturnAListOfFiveUsers() throws Exception {
        List<User> users = userRepository.findAll();

        assertThat(users.size(), is(5));
        assertThat(users, containsInAnyOrder(
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
    public void delete_ShouldDeleteAnExistingUser() throws Exception {
        userRepository.delete(5L);
    }
}
