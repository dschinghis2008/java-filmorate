package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public void createUser(@RequestBody User user) throws ValidateException {
        if(users.containsKey(user.getId())){
            throw new ValidateException("пользователь с таким id уже зарегистрирован");
        }
        if(!user.getEmail().contains("@") || user.getEmail().isBlank() || user.getEmail() == null){
            throw new ValidateException("неправильный формат email или пустой email");
        }
        if(user.getLogin().contains(" ") || user.getLogin().isBlank() || user.getLogin() == null){
            throw new ValidateException("пустой логин или содержит пробелы");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidateException("дата рождения указывает на будущее время");
        }
        users.put(user.getId(),user);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) throws ValidateException {
        if(!users.containsKey(user.getId())){
            throw new ValidateException("не найден пользователь для обновления его персональных данных");
        }
        users.put(user.getId(), user);
    }

    @GetMapping
    public String getUsers(){
        String result = "";
        for(Integer id : users.keySet()){
            result += users.get(id).getEmail() + " " + users.get(id).getBirthday();
            if(users.get(id).getName() == null || users.get(id).getName().isBlank()){
                result += " " + users.get(id).getLogin();
            } else {
                result += " " + users.get(id).getName() + "\n";
            }
        }
        return result;
    }
}
