package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long id1, Long id2) {
        if (userStorage.getById(id1) != null && userStorage.getById(id2) != null
                && !userStorage.getById(id1).getName().equals("") && !userStorage.getById(id2).getName().equals("")) {
            userStorage.getById(id1).addFriend(id2);
            userStorage.getById(id2).addFriend(id1);
        } else {
            throw new ValidateException("user с id=" + id1 + " или id=" + id2 + " не найден");
        }

    }

    public void removeFriend(Long id1, Long id2) {
        if (userStorage.getById(id1) != null && userStorage.getById(id2) != null) {
            userStorage.getById(id1).removeFriend(id2);
            userStorage.getById(id2).removeFriend(id1);
        } else {
            throw new ValidateException("user с id=" + id1 + " или id=" + id2 + " не найден");
        }
    }

    public List<User> getFriends(Long id) {
        List<User> list = new ArrayList<>();
        for (Long idUser : userStorage.getById(id).getFriends()) {
            list.add(userStorage.getById(idUser));
        }
        return list;
    }

    public List<User> getCommonFriends(Long id1, Long id2) {
        List<User> commonFriends = new ArrayList<>();
        for (Long id : userStorage.getById(id1).getFriends()) {
            if (userStorage.getById(id2).getFriends().contains(id)) {
                commonFriends.add(userStorage.getById(id));
            }
        }
        return commonFriends;
    }
}
