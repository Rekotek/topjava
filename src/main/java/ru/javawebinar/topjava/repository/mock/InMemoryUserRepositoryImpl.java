package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.util.exception.DuplicatedEmailException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        UserUtil.USERS.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        User userRemoved = repository.remove(id);
        return userRemoved != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        boolean eMailIsPresent = repository.values().stream()
                .anyMatch(u -> user.getEmail().equals(u.getEmail()));

        if (eMailIsPresent) {
            throw new DuplicatedEmailException("Duplicated email: " + user.getEmail());
        }

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
        Comparator<AbstractNamedEntity> nameThenIdComparator =
                Comparator.comparing(AbstractNamedEntity::getName)
                        .thenComparing(AbstractBaseEntity::getId);
        return repository.values().stream()
                .sorted(nameThenIdComparator)
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return repository.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst().orElse(null);
    }
}
