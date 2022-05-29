package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidateException {

        userStorage.createUser(user);
        log.debug("добавлен user: {}", user.getId());
        return user;
    }

    @PutMapping
    public void updateUser(@RequestBody User user) throws ValidateException {
        userStorage.updateUser(user);
        log.debug("обновлен user: {}", user.getId());
    }

    @GetMapping
    public Map<Long,User> getUsers() {
        log.debug("запрошены все пользователи");
        return userStorage.getAll();
    }

    public int getCountUsers() {
        return userStorage.getAll().size();
    }

    public void clearUsers() {
        userStorage.getAll().clear();
    }

    @PutMapping ("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id,@PathVariable Long friendId){
        userService.addFriend(id,friendId);
        log.debug("Добавлены в друзья {}, {}",id,friendId);
    }

    @DeleteMapping ("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id,@PathVariable Long friendId){
        userService.removeFriend(id,friendId);
        log.debug("Удалены из друзей {}, {}",id,friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<Long> getFriends(@PathVariable Long id){
        log.debug("Запрошен список друзей id={}",id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<Long> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId){
        log.debug("Запрошен список общих друзей id1={},id2={}",id,otherId);
        return userService.getCommonFriends(id,otherId);
    }
}
