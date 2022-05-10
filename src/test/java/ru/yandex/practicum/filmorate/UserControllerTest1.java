package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc

public class UserControllerTest1 { //example integration test

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;
    @Test
    void createValidUser() throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setBirthday(LocalDate.of(2000, 1, 1));
        user1.setName("User1");
        user1.setLogin("user1");
        user1.setEmail("user1@example");
        String body = mapper.writeValueAsString(user1);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
