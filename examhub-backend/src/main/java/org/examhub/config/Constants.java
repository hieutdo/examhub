package org.examhub.config;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * @author Hieu Do
 */
public final class Constants {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static final String FORM_LOGIN_PARAM_USERNAME = "username";
    public static final String FORM_LOGIN_PARAM_PASSWORD = "password";

    private Constants() {
    }
}
