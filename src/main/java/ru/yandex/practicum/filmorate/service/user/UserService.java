package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long id1, Long id2) {
        if (userStorage.getById(id1) != null && userStorage.getById(id2) != null) {
            userStorage.getById(id1).addFriend(id2);
            userStorage.getById(id2).addFriend(id1);
        } else {
            throw new ValidateException("user с id=" + id1 + " или id=" + id2 + " не существует");
        }

    }

    public void removeFriend(Long id1, Long id2) {
        if (userStorage.getById(id1) != null && userStorage.getById(id2) != null) {
            userStorage.getById(id1).removeFriend(id2);
            userStorage.getById(id2).removeFriend(id1);
        } else {
            throw new ValidateException("user с id=" + id1 + " или id=" + id2 + " не существует");
        }
    }

    public Set<Long> getFriends(Long id) {
        return userStorage.getById(id).getFriends();
    }

    public Set<Long> getCommonFriends(Long id1, Long id2) {
        Set<Long> commonFriends = new HashSet<>();
        for (Long id : userStorage.getById(id1).getFriends()) {
            if (userStorage.getById(id2).getFriends().contains(id)) {
                commonFriends.add(id);
            }
        }
        return commonFriends;
    }
}
