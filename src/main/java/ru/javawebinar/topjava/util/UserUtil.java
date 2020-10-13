package ru.javawebinar.topjava.util;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

/**
 * @author gulnaz
 */
public class UserUtil {
    public static final Integer ADMIN_ID = 1;
    public static final Integer USER_ID = 2;

    public static final List<User> users = Arrays.asList(
        new User(ADMIN_ID, "admin", "admin@example.com", "admin", MealsUtil.DEFAULT_CALORIES_PER_DAY, true, EnumSet.of(Role.ADMIN)),
        new User(USER_ID, "user", "user@example.com", "user", 2500, true, EnumSet.of(Role.USER))
    );
}
