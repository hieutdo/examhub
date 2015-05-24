package org.examhub.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Hieu Do
 */
public class UsernameAlreadyExistsException extends BaseRestException {
    private static final long serialVersionUID = 3558003189721480863L;

    public UsernameAlreadyExistsException(String username) {
        super(
            HttpStatus.BAD_REQUEST,
            "40001",
            "The username '" + username + "' already exists. Please choose another one."
        );
    }
}
