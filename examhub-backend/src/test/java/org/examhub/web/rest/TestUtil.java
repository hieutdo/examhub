package org.examhub.web.rest;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

/**
 * @author Hieu Do
 */
public class TestUtil {
    /**
     * MediaType for JSON UTF8
     */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
        MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

}
