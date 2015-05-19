package org.examhub.web.rest;

import org.examhub.domain.UserAccount;
import org.examhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Hieu Do
 */
@RestController
@RequestMapping("/api/v1")
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<UserAccount> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public UserAccount getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> registerUserAccount(@Valid @RequestBody UserAccount userAccount) {
        UserAccount existingUser = userService.getUserByUsername(userAccount.getUsername());
        if (existingUser != null) {
            return new ResponseEntity<>("username already exist", HttpStatus.BAD_REQUEST);
        }
        existingUser = userService.getUserByEmail(userAccount.getEmail());
        if (existingUser != null) {
            return new ResponseEntity<>("email already exist", HttpStatus.BAD_REQUEST);
        }
        userService.createNewUser(userAccount.getUsername(), userAccount.getPassword(), userAccount.getEmail());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
