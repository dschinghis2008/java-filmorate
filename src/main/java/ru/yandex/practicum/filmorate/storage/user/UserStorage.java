package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {
    User createUser(User user);
    User updateUser(User user);
    List<User> getAll();
    void deleteUser(int id);
    void deleteAll();
    User getById(Long id);
}
