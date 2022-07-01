package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
    @AutoConfigureTestDatabase
    @RequiredArgsConstructor(onConstructor_ = @Autowired)
    class FilmoRateApplicationTests {
        private final DbUserStorage userStorage;

        @Test
        public void testFindUserById() {

            Optional<User> userOptional = userStorage.getById(1L);

            assertThat(userOptional)
                    .isPresent()
                    .hasValueSatisfying(user ->
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                    );
        }

        @Test
        public void testGetAllUsers(){
            List<User> users = userStorage.getAll();
            assertThat(users.size())
                    .isEqualTo(3);
        }
    }
