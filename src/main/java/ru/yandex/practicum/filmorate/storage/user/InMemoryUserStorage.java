package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long newId = 1L;

    private Long getNewId() {
        return newId++;
    }

    public User createUser(User user) {
        try {
            if (user.getName().equals("") || user.getName() == null) {
                user.setName(user.getLogin());
            }
            if (!user.getEmail().contains("@") || user.getEmail().isBlank() || user.getEmail() == null) {
                throw new ValidateException("неправильный формат email или пустой email");
            }
            if (user.getLogin().contains(" ") || user.getLogin().isBlank() || user.getLogin() == null) {
                throw new ValidateException("пустой логин или содержит пробелы");
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidateException("дата рождения указывает на будущее время");
            }

        } catch (ValidateException e) {
            throw new RuntimeException(e);
        }
        user.setId(getNewId());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() <= 0 || user.getId() == null) {
            throw new ValidateException("id должен быть > 0");
        }
        users.put(user.getId(), user);
        return user;
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        for (Long id : users.keySet()) {
            list.add(users.get(id));
        }
        return list;
    }

    public void deleteUser(int id) {
        users.remove(id);
    }

    public void deleteAll() {
        users.clear();
    }

    public User getById(Long id) {
        if (users.get(id) == null) {
            throw new ValidateException("user not found");
        }
        return users.get(id);
    }
}
