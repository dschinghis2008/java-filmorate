package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public void createUser(@RequestBody User user) throws ValidateException {
        if (!user.getEmail().contains("@") || user.getEmail().isBlank() || user.getEmail() == null) {
            throw new ValidateException("неправильный формат email или пустой email");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidateException("пустой логин или содержит пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("дата рождения указывает на будущее время");
        }
        users.put(user.getId(), user);
        System.out.println(user);
        log.debug("добавлен user: " + user.toString());
    }

    @PutMapping
    public void updateUser(@RequestBody User user) throws ValidateException {
        if (!users.containsKey(user.getId())) {
            throw new ValidateException("не найден пользователь для обновления его персональных данных");
        }
        users.put(user.getId(), user);
        log.debug("обновлен user: " + user.toString());
    }

    @GetMapping
    public String getUsers() {
        String result = "";
        for (Integer id : users.keySet()) {
            result += users.get(id).getEmail() + " " + users.get(id).getBirthday();
            if (users.get(id).getName() == null || users.get(id).getName().isBlank()) {
                result += " " + users.get(id).getLogin();
            } else {
                result += " " + users.get(id).getName() + "\n";
            }
        }
        log.debug("запрошены users: " + result);
        return result;
    }

    public int getCountUsers() {
        return users.size();
    }

    public void clearUsers(){
        users.clear();
    }
}
