package org.examhub.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Hieu Do
 */
public class EmailAlreadyExistsException extends BaseRestException {
    private static final long serialVersionUID = 250052210319305795L;

    public EmailAlreadyExistsException(String email) {
        super(
            HttpStatus.BAD_REQUEST,
            "40002",
            "The email address '" + email + "' already exists. Please choose another one."
        );
    }
}
