package ru.javawebinar.topjava.repository.jdbc;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;

/**
 * @author gulnaz
 */
@Repository
@Profile(Profiles.POSTGRES_DB)
public class JdbcForPostgresMealRepository extends JdbcMealRepository {

    public JdbcForPostgresMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public LocalDateTime convertDateTime(LocalDateTime localDateTime) {
        return localDateTime;
    }
}
