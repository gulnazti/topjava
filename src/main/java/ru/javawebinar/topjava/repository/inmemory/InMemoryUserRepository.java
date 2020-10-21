package ru.javawebinar.topjava.repository.inmemory;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import static ru.javawebinar.topjava.util.MealsUtil.ADMIN_ID;
import static ru.javawebinar.topjava.util.MealsUtil.USER_ID;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(USER_ID);

    {
        User admin = new User(ADMIN_ID, "admin", "admin@example.com", "admin", MealsUtil.DEFAULT_CALORIES_PER_DAY, true, EnumSet.of(Role.ADMIN));
        User user = new User(USER_ID, "user", "user@example.com", "user", 2500, true, EnumSet.of(Role.USER));
        repository.put(ADMIN_ID, admin);
        repository.put(USER_ID, user);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }

        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return repository.values().stream()
            .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
            .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return getAll().stream()
            .filter(user -> user.getEmail().equals(email))
            .findAny()
            .orElse(null);
    }
}
