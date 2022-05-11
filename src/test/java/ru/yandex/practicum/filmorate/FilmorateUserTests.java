package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateUserTests {

    @Autowired
    private UserController userController;


    @Test
    public void userControllerValidEntityTest() throws ValidateException {
        User user = new User();
        user.setId(1);
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("User1");
        user.setLogin("user1");
        user.setEmail("user1@example");

        userController.clearUsers();
        userController.createUser(user);
        Assertions.assertEquals(userController.getCountUsers(), 1, "ожидается - добавлен 1 юсер");
    }

    @Test
    void userControllerInvalidEmailTest() {
        User user1 = new User();
        user1.setId(1);
        user1.setBirthday(LocalDate.of(2000, 1, 1));
        user1.setName("User1");
        user1.setLogin("user1");
        user1.setEmail("user1.example");

        assertThrows(ValidateException.class, () -> userController.createUser(user1));

    }

    @Test
    public void userControllerInvalidLoginTest() {
        User user = new User();
        user.setId(2);
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("User2");
        user.setLogin("");
        user.setEmail("user2@example");

        assertThrows(ValidateException.class, () -> userController.createUser(user));
        user.setLogin(null);
        assertThrows(NullPointerException.class, () -> userController.createUser(user));
        user.setLogin("user 2");
        assertThrows(ValidateException.class, () -> userController.createUser(user));

    }

    @Test
    public void userControllerInvalidDateBirthTest() {
        User user = new User();
        user.setId(2);
        user.setBirthday(LocalDate.of(2030, 1, 1));
        user.setName("User2");
        user.setLogin("user2");
        user.setEmail("user2@example");

        assertThrows(ValidateException.class, () -> userController.createUser(user));
    }

    @Test
    public void userControllerUpdateTest() throws ValidateException {
        User user = new User();
        user.setId(1);
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("User1");
        user.setLogin("user1");
        user.setEmail("user1@example");

        User userUpd = new User();
        userUpd.setId(1);
        userUpd.setBirthday(LocalDate.of(2002, 1, 1));
        userUpd.setName("UserNew");
        userUpd.setLogin("userNew");
        userUpd.setEmail("userNew@example");

        userController.clearUsers();
        userController.createUser(user);
        userController.updateUser(userUpd);
        Assertions.assertEquals(userController.getCountUsers(), 1, "ожидается - обновлен 1 юсер");
    }

    @Test
    public void userControllerGetTest() throws IOException, InterruptedException, ValidateException {
        User user = new User();
        user.setId(1);
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("User1");
        user.setLogin("user1");
        user.setEmail("user1@example");

        User user2 = new User();
        user2.setId(2);
        user2.setBirthday(LocalDate.of(2002, 1, 1));
        user2.setName("UserNew");
        user2.setLogin("userNew");
        user2.setEmail("userNew@example");

        userController.createUser(user);
        userController.createUser(user2);
        String[] users = userController.getUsers().split("\n");
        Assertions.assertEquals(users.length, 2, "ожидается - получили 2 юсера");
    }


}
