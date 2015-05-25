package org.examhub.support;

import org.examhub.exception.BaseRestException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for all {@code @RestController}, producing JSON error messages for rest api.
 * This class also extends {@link DefaultErrorAttributes} to add more attributes to Spring Boot's
 * default error attributes.
 * <p>The returned JSON has the following information:</p>
 * <ul>
 * <li>status - http status code (e.g. 400, 500)</li>
 * <li>error - error reason (e.g. Bad Request, Internal Server Error)</li>
 * <li>code - api specific error code (e.g. 400001, 500001)</li>
 * <li>message - message embedded in thrown exception</li>
 * <li>developerMessage - technical information for developer calling the api</li>
 * <li>exception - class name of the root exception</li>
 * <li>errors - any {@link ObjectError}s from a {@link BindingResult} exception
 * <li>trace - exception stack trace</li>
 * <li>path - URL path when the exception was raised</li>
 * <li>moreInfo - URL to web page describes the error fully and possible solutions to help them resolve it</li>
 * <li>timestamp - the time that the errors were extracted</li>
 * </ul>
 *
 * @author Hieu Do
 * @see DefaultErrorAttributes
 */
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandlerAdvice extends DefaultErrorAttributes {

    private Map<String, Object> createCustomErrorAttributes(RequestAttributes requestAttributes) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        Throwable error = getError(requestAttributes);

        if (error != null && error instanceof BaseRestException) {
            BaseRestException exception = (BaseRestException) error;
            String code = exception.getCode();
            String developerMessage = exception.getDeveloperMessage();
            String moreInfo = exception.getMoreInfo();
            HttpStatus httpStatus = exception.getHttpStatus();

            if (httpStatus != null) {
                errorAttributes.put("status", httpStatus.value());
                errorAttributes.put("error", httpStatus.getReasonPhrase());
                requestAttributes.setAttribute("javax.servlet.error.status_code", httpStatus.value(), RequestAttributes.SCOPE_REQUEST);
            }
            if (StringUtils.hasText(code)) {
                errorAttributes.put("code", code);
            }
            if (StringUtils.hasText(developerMessage)) {
                errorAttributes.put("developerMessage", exception.getDeveloperMessage());
            }
            if (StringUtils.hasText(moreInfo)) {
                errorAttributes.put("moreInfo", exception.getMoreInfo());
            }
        }

        return errorAttributes;
    }

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
        errorAttributes.putAll(this.createCustomErrorAttributes(requestAttributes));
        return errorAttributes;
    }

    private ResponseEntity<Map<String, Object>> handleExceptionInternal(HttpServletRequest request, HttpStatus httpStatus) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Map<String, Object> body = this.getErrorAttributes(requestAttributes, false);
        return new ResponseEntity<>(body, httpStatus);
    }

    /**
     * Provides handling for standard Spring MVC exceptions.
     *
     * @param ex       the target exception
     * @param response the current response
     * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
     */
    @ExceptionHandler(value = {
        NoSuchRequestHandlingMethodException.class,
        HttpRequestMethodNotSupportedException.class,
        HttpMediaTypeNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        MissingServletRequestParameterException.class,
        ServletRequestBindingException.class,
        ConversionNotSupportedException.class,
        TypeMismatchException.class,
        HttpMessageNotReadableException.class,
        HttpMessageNotWritableException.class,
        MethodArgumentNotValidException.class,
        MissingServletRequestPartException.class,
        BindException.class,
        NoHandlerFoundException.class
    })
    protected void handleSpringMvcExceptions(Exception ex, HttpServletResponse response) throws IOException {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof NoSuchRequestHandlingMethodException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            status = HttpStatus.NOT_ACCEPTABLE;
        } else if (ex instanceof MissingServletRequestParameterException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ServletRequestBindingException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ConversionNotSupportedException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof TypeMismatchException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof HttpMessageNotReadableException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof HttpMessageNotWritableException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof MethodArgumentNotValidException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof MissingServletRequestPartException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof BindException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof NoHandlerFoundException) {
            status = HttpStatus.NOT_FOUND;
        }
        response.sendError(status.value());
    }

    /**
     * Send a 409 Conflict in case of concurrent modification.
     *
     * @param response the current response
     */
    @ExceptionHandler({
        ObjectOptimisticLockingFailureException.class,
        OptimisticLockingFailureException.class,
        DataIntegrityViolationException.class
    })
    protected void handleConflict(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }

    /**
     * Send a 500 Internal Server Error in case an exception has no handler matched
     *
     * @param request the current request
     */
    @ExceptionHandler({Throwable.class})
    protected ResponseEntity<Map<String, Object>> handleThrowable(HttpServletRequest request) {
        return handleExceptionInternal(request, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
