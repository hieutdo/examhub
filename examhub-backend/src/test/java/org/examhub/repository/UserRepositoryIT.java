package org.examhub.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.examhub.ExamHubApplication;
import org.examhub.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

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
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup(UserRepositoryIT.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { UserRepositoryIT.DATASET })
@DirtiesContext
public class UserRepositoryIT {

    protected static final String DATASET = "classpath:datasets/it-users.xml";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findOneByUsername_ShouldReturnExistingUser() throws Exception {
        User user = userRepository.findOneByUsername("user1");

        assertThat(user, notNullValue());
    }

    @Test
    public void findAll_TwoUsersFound_ShouldReturnAListOfTwoUsers() throws Exception {
        List<User> users = userRepository.findAll();

        assertThat(users.size(), is(1));
        assertThat(users, contains(
            allOf(
                hasProperty("id", is(1L)),
                hasProperty("username", is("user1")),
                hasProperty("firstName", is("john")),
                hasProperty("lastName", is("doe"))
            )
        ));
    }
}
