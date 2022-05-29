package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public User createUser(User user) {

        try {
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
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public Map<Long,User> getAll(){
        return users;
    }

    public void deleteUser(int id){
        users.remove(id);
    }

    public void deleteAll(){
        users.clear();
    }

    public User getById(Long id){
        return users.get(id);
    }
}
