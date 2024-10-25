package com.example.jwt_demo.repository;

import com.example.jwt_demo.model.User;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter.incrementAndGet());
        }
        users.put(user.getEmail(), user);
        return user;
    }

    public boolean existsByEmail(String email) {
        return users.containsKey(email);
    }

    public List<User> getAllUsers(){
        return users.values().stream().toList();
    }
}