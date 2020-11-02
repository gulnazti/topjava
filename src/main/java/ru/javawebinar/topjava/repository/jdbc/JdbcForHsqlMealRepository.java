package ru.javawebinar.topjava.repository.jdbc;

import java.sql.Timestamp;
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
@Profile(Profiles.HSQL_DB)
public class JdbcForHsqlMealRepository extends JdbcMealRepository {

    public JdbcForHsqlMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public Timestamp convertDateTime(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
