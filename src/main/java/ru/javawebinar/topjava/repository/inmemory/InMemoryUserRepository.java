package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private static Map<Integer, User> repository = new ConcurrentHashMap<>();

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);

        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);


        repository.put(user.getId(), user);

        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);


        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");


        return new ArrayList<>(repository.values());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);

        User userByEmail = null;

        for (User user : repository.values()) {

            if (user.getEmail().equals(email)) {
                userByEmail = user;
            }


        }

        return userByEmail;
    }
}
