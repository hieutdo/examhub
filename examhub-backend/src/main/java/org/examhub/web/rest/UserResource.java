package org.examhub.web.rest;

import org.examhub.domain.UserAccount;
import org.examhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Hieu Do
 */
@RestController
@RequestMapping("/api/v1")
public class UserResource {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<UserAccount> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public UserAccount getUser(@PathVariable String username) {
        return userService.getUser(username);
    }
}
