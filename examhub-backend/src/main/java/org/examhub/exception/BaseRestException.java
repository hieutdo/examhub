package org.examhub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

/**
 * @author Hieu Do
 */
public class BaseRestException extends RuntimeException {
    private static final long serialVersionUID = 7448415552980308260L;

    private final HttpStatus httpStatus;
    private final String code;
    private final String developerMessage;
    private final String moreInfo;

    public BaseRestException(String message) {
        this(null, null, message, null, null);
    }

    public BaseRestException(String code, String message) {
        this(null, code, message, null, null);
    }

    public BaseRestException(HttpStatus httpStatus, String code, String message) {
        this(httpStatus, code, message, null, null);
    }

    public BaseRestException(HttpStatus httpStatus, String code, String message, String developerMessage) {
        this(httpStatus, code, message, developerMessage, null);
    }

    public BaseRestException(HttpStatus httpStatus, String code, String message, String developerMessage, String moreInfo) {
        super(message);
        this.httpStatus = httpStatus;
        this.developerMessage = developerMessage;
        this.moreInfo = moreInfo;

        // code is default to http status
        if (!StringUtils.hasText(code) && httpStatus != null) {
            this.code = httpStatus.toString();
        } else {
            this.code = code;
        }
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public String getMoreInfo() {
        return moreInfo;
    }
}
