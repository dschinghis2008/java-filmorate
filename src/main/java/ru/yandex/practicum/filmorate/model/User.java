package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String email;
    private String name;
    private String login;
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

    public void addFriend(Long id){
        friends.add(id);
    }

    public void removeFriend(Long id){
        friends.remove(id);
    }

}
