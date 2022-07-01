package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long newId = 1L;

    private Long getNewId() {
        return newId++;
    }

    public Optional<User> createUser(User user) {
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
        return Optional.of(user);
    }

    public Optional<User> updateUser(User user) {
        if (user.getId() <= 0 || user.getId() == null) {
            throw new ValidateException("id должен быть > 0");
        }
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        for (Long id : users.keySet()) {
            list.add(users.get(id));
        }
        return list;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    public void deleteAll() {
        users.clear();
    }

    public Optional<User> getById(Long id) {
        if (users.get(id) == null) {
            throw new ValidateException("user not found");
        }
        return Optional.of(users.get(id));
    }

    public void addFriend(Long id, Long idFriend) {
    }

    @Override
    public void deleteFriend(Long id, Long idFriend) {

    }

    @Override
    public List<User> getFriends(Long id) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long idFriend) {
        return null;
    }

    public void deleteFriend(Long id) {
    }

    public List<User> getFriends() {
        return null;
    }

    public List<User> getCommonFriends() {
        return null;
    }
}
