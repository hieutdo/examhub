package org.examhub.web.rest;

import org.examhub.config.Constants;
import org.examhub.domain.User;
import org.examhub.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Hieu Do
 */
@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserResource userResource;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = standaloneSetup(userResource).build();
    }

    @Test
    public void findAll_shouldReturnExistingUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setFirstName("john");
        user1.setLastName("doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setFirstName("jane");
        user2.setLastName("doe");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(Constants.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].username", is("user1")))
            .andExpect(jsonPath("$[0].firstName", is("john")))
            .andExpect(jsonPath("$[0].lastName", is("doe")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].username", is("user2")))
            .andExpect(jsonPath("$[1].firstName", is("jane")))
            .andExpect(jsonPath("$[1].lastName", is("doe")));

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }
}
