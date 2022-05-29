package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private final Set<Long> likes = new HashSet<>();

    public void addLike(Long idUser){
        likes.add(idUser);
    }

    public void removeLike(Long idUser){
        likes.remove(idUser);
    }

    public int countLikes(){
        return likes.size();
    }
}
