package org.examhub.repository;

import org.examhub.ExamHubApplication;
import org.examhub.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Hieu Do
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExamHubApplication.class)
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findOneByUsername_ShouldReturnExistingUser() throws Exception {
        User user = userRepository.findOneByUsername("user1");

        assertThat(user, notNullValue());
    }
}
