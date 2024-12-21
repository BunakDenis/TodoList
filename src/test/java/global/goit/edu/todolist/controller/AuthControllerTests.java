package global.goit.edu.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.goit.edu.todolist.TodoListTestBaseClass;
import global.goit.edu.todolist.model.entity.message.AuthMessage;
import global.goit.edu.todolist.model.entity.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
public class AuthControllerTests extends TodoListTestBaseClass {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testThatMethodRegistrationReturnMessageOk() throws Exception {

        //Given
        String request = "{\"username\": \"user\", \"password\": \"jdbcDefault\"}";

        //When
        String response = mvc.perform(post("/auth/reg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String message = objectMapper.readTree(response).get("message").asText();

        //Then
        Assertions.assertEquals(AuthMessage.ok.name(), message);

    }

    @Test
    public void testThatMethodLoginReturnToken() throws Exception {

        //Given
        User user = userService.create(getCurrentUser());

        String request = "{\"username\": \"user\", \"password\": \"jdbcDefault\"}";

        //When
        String response = mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String message = objectMapper.readTree(response).get("message").asText();
        String token = objectMapper.readTree(response).get("authToken").asText();

        //Then
        Assertions.assertEquals(AuthMessage.ok.name(), message);
        Assertions.assertTrue(jwtService.validateToken(token, user));

    }

    private User getCurrentUser() {
        return User.builder()
                .username("user")
                .password("$2a$12$kpU9BuqBbOyUvPo89aNRReiNFFSBYQ9zfecLhsTgYLlwFrHuisYoi")
                .build();
    }

}
