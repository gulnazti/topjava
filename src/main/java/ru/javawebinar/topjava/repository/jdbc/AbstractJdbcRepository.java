package ru.javawebinar.topjava.repository.jdbc;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author gulnaz
 */
abstract class AbstractJdbcRepository<T> {

    protected final JdbcTemplate jdbcTemplate;

    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected final Validator validator;

    public AbstractJdbcRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    protected void validate(T bean) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            String messages = violations.stream()
                .map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage()))
                .collect(Collectors.joining("; "));
            throw new ValidationException(messages);
        }
    }
}
