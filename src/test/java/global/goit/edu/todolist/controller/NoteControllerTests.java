package global.goit.edu.todolist.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import global.goit.edu.todolist.TodoListTestBaseClass;
import global.goit.edu.todolist.model.entity.dto.oparation.Operation;
import global.goit.edu.todolist.model.entity.message.NoteMessage;
import global.goit.edu.todolist.model.entity.note.Note;
import global.goit.edu.todolist.model.entity.user.User;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteControllerTests extends TodoListTestBaseClass {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String token;

    @BeforeEach
    public void login() throws Exception {

        try {
            userService.create(getCurrentUser());
        } catch (IllegalArgumentException e) {

        }

        try {
            getNotes().forEach(n -> {
                noteService.create(n);
            });
        } catch (IllegalArgumentException e) {

        }

        String request = "{\"username\": \"user\", \"password\": \"jdbcDefault\"}";

        String response = mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        token = objectMapper.readTree(response).get("authToken").asText();
    }

    @Test
    @Order(1)
    public void testThatMethodAddNoteWorkOk() throws Exception {

        //Given
        Cookie cookie = getCookie();
        Note expected = Note.builder()
                .id(3l)
                .user(getCurrentUser())
                .content("Java EE")
                .title("The most beautiful language in the world!")
                .build();

        String jsonNote = objectMapper.writeValueAsString(expected);

        //When
        String response = mvc.perform(post("/note/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
                        .content(jsonNote))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Note> actuals = getNotesFromResponse(response);
        Note actual = new Note();
        for (Note note : actuals) {
            if (note.getTitle().equals(expected.getTitle())) {
                actual = note;
            }
        }

        String actualOperation = objectMapper.readTree(response).get("operation").asText();
        String actualMessage = objectMapper.readTree(response).get("message").asText();


        //Then
        Assertions.assertEquals(Operation.CREATE.name(), actualOperation);
        Assertions.assertEquals(NoteMessage.OK.name(), actualMessage);

        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    @Order(2)
    public void testThatMethodGetAllNotesWorkOk() throws Exception {

        //Given
        Cookie cookie = getCookie();

        //When
        String response = mvc.perform(get("/note/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Note> actual = getNotesFromResponse(response);

        String actualOperation = objectMapper.readTree(response).get("operation").asText();
        String actualMessage = objectMapper.readTree(response).get("message").asText();

        //Then
        Assertions.assertEquals(Operation.READ.name(), actualOperation);
        Assertions.assertEquals(NoteMessage.OK.name(), actualMessage);

        Assertions.assertEquals(3, actual.size());

    }

    @Test
    @Order(3)
    public void testThatMethodGetNoteByIdInPathWorkOk() throws Exception {

        //Given
        Cookie cookie = getCookie();
        Note expected = getNotes().get(0);

        //When
        String response = mvc.perform(get("/note/get/1")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Note> actuals = getNotesFromResponse(response);

        String actualOperation = objectMapper.readTree(response).get("operation").asText();
        String actualMessage = objectMapper.readTree(response).get("message").asText();

        //Then
        Assertions.assertEquals(Operation.READ.name(), actualOperation);
        Assertions.assertEquals(NoteMessage.OK.name(), actualMessage);

        Assertions.assertEquals(expected.getId(), actuals.get(0).getId());
        Assertions.assertEquals(expected.getTitle(), actuals.get(0).getTitle());
        Assertions.assertEquals(expected.getContent(), actuals.get(0).getContent());
    }

    @Test
    @Order(4)
    public void testThatMethodEditNoteWorkOk() throws Exception {

        //Given
        Cookie cookie = getCookie();
        Note expected = noteService.getById(1);

        expected.setTitle("Mystic way");
        expected.setContent("Cost of right mystic way is very expensive");
        String noteForContent = objectMapper.writeValueAsString(expected);

        //When
        String response = mvc.perform(post("/note/edit")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noteForContent))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Note> actuals = getNotesFromResponse(response);

        String actualOperation = objectMapper.readTree(response).get("operation").asText();
        String actualMessage = objectMapper.readTree(response).get("message").asText();

        //Then
        Assertions.assertEquals(Operation.UPDATE.name(), actualOperation);
        Assertions.assertEquals(NoteMessage.OK.name(), actualMessage);

        Assertions.assertEquals(expected.getId(), actuals.get(0).getId());
        Assertions.assertEquals(expected.getTitle(), actuals.get(0).getTitle());
        Assertions.assertEquals(expected.getContent(), actuals.get(0).getContent());
    }

    @Test
    @Order(5)
    public void testThatMethodDeleteByIdWorkOk() throws Exception {

        //Given
        Cookie cookie = getCookie();
        Note expected = noteService.getById(1);

        //When
        String response = mvc.perform(post("/note/delete/1")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Note> actuals = noteService.getAll();
        String actualOperation = objectMapper.readTree(response).get("operation").asText();
        String actualMessage = objectMapper.readTree(response).get("message").asText();

        //Then
        Assertions.assertEquals(Operation.DELETE.name(), actualOperation);
        Assertions.assertEquals(NoteMessage.OK.name(), actualMessage);
        actuals.forEach(n -> {
            Assertions.assertFalse(n.getId() == expected.getId());
            Assertions.assertFalse(n.getUser() == expected.getUser());
            Assertions.assertFalse(n.getTitle() == expected.getTitle());
            Assertions.assertFalse(n.getContent() == expected.getContent());
        });
    }

    private List<Note> getNotesFromResponse(String response) throws IOException {
        JsonNode arrNote = objectMapper.readTree(response).get("notes");
        ObjectReader reader = objectMapper.readerFor(new TypeReference<List<Note>>() {
        });

        List<Note> result = reader.readValue(arrNote);
        return result;
    }

    private Cookie getCookie() {
        Cookie cookie = new Cookie("authorization", token);
        cookie.setMaxAge(600);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        return cookie;
    }

    private static User getCurrentUser() {
        return User.builder()
                .username("user")
                .password("$2a$12$kpU9BuqBbOyUvPo89aNRReiNFFSBYQ9zfecLhsTgYLlwFrHuisYoi")
                .build();
    }

    private static List<Note> getNotes() {
        List<Note> result = new ArrayList<>();

        result.add(
                Note.builder()
                        .id(1)
                        .user(getCurrentUser())
                        .title("Do Androids Dream of Electric Sheep?")
                        .content("It was January 2021, and Rick Deckard had a license to kill. " +
                                "Somewhere among the hordes of humans out there, lurked several rogue androids.")
                        .build()
        );

        result.add(
                Note.builder()
                        .id(2)
                        .user(getCurrentUser())
                        .title("The Hitchhiker’s Guide to the Galaxy")
                        .content("Seconds before the Earth is demolished to make way for a galactic freeway, " +
                                "Arthur Dent is plucked off the planet by his friend Ford Prefect, a researcher for " +
                                "the revised edition of The Hitchhikers Guide to the Galaxy who, for the last" +
                                " fifteen years, has been posing as an out-of-work actor.")
                        .build()
        );
        return result;
    }

}
