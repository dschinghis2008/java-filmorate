package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("dbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long id1, Long id2) {
        if (userStorage.getById(id1) != null && userStorage.getById(id2) != null
                && !(id1 <= 0) && !(id2 <= 0)) {
            userStorage.addFriend(id1, id2);
        } else {
            throw new NotFoundException("user с id=" + id1 + " или id=" + id2 + " не найден");
        }


    }

    public void removeFriend(Long id1, Long id2) {
        if (userStorage.getById(id1) != null && userStorage.getById(id2) != null
                && !(id1 <= 0) && !(id2 <= 0)) {
            userStorage.deleteFriend(id1, id2);
        } else {
            throw new NotFoundException("user с id=" + id1 + " или id=" + id2 + " не найден");
        }
    }

    public List<User> getFriends(Long id) {
        List<User> list = userStorage.getFriends(id);
        return list;
    }

    public List<User> getCommonFriends(Long id1, Long id2) {
        List<User> commonFriends = userStorage.getCommonFriends(id1, id2);
        return commonFriends;
    }
}
