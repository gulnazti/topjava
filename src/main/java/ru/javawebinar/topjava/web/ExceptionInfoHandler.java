package ru.javawebinar.topjava.web;

import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.util.ValidationUtil.DUPLICATE_DATETIME_CODE;
import static ru.javawebinar.topjava.util.ValidationUtil.DUPLICATE_EMAIL_CODE;
import static ru.javawebinar.topjava.util.ValidationUtil.getErrors;
import static ru.javawebinar.topjava.util.ValidationUtil.getRootCause;
import static ru.javawebinar.topjava.util.exception.ErrorType.APP_ERROR;
import static ru.javawebinar.topjava.util.exception.ErrorType.DATA_ERROR;
import static ru.javawebinar.topjava.util.exception.ErrorType.DATA_NOT_FOUND;
import static ru.javawebinar.topjava.util.exception.ErrorType.VALIDATION_ERROR;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private static final Map<String, String> codes = Map.of(
        "unique_email", DUPLICATE_EMAIL_CODE,
        "unique_user_datetime", DUPLICATE_DATETIME_CODE);

    @Autowired
    private MessageResolver messageResolver;

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String errorMsg = getRootCause(e).getMessage();
        Optional<Map.Entry<String, String>> error = codes.entrySet()
            .stream()
            .filter(entry -> errorMsg.contains(entry.getKey()))
            .findAny();

        return error.map(entry -> logAndGetErrorInfo(req, e, true, DATA_ERROR,
            messageResolver.getMessage(entry.getValue())
        )).orElseGet(() -> logAndGetErrorInfo(req, e, true, DATA_ERROR));
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({BindException.class})
    public ErrorInfo bindingError(HttpServletRequest req, BindException e) {
        BindingResult result = e.getBindingResult();
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, getErrors(result));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String... errors) {
        Throwable rootCause = getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return new ErrorInfo(req.getRequestURL(), errorType, messageResolver.getMessage(errorType.getCode()),
            errors.length != 0 ? errors : new String[]{rootCause.getLocalizedMessage()});
    }
}