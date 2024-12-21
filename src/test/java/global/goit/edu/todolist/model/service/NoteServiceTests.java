package global.goit.edu.todolist.model.service;

import global.goit.edu.todolist.TodoListTestBaseClass;
import global.goit.edu.todolist.model.entity.message.NoteMessage;
import global.goit.edu.todolist.model.entity.note.Note;
import global.goit.edu.todolist.model.entity.user.User;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Rollback(value = false)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteServiceTests extends TodoListTestBaseClass {

    private static Note expected;

    @BeforeAll
    public static void beforeAll() {
        expected = Note.builder()
                .id(3L)
                .user(
                        User.builder()
                                .username("test_user")
                                .password("$2a$12$kpU9BuqBbOyUvPo89aNRReiNFFSBYQ9zfecLhsTgYLlwFrHuisYoi")
                                .build()
                )
                .title("Geodesy")
                .content("Geodesy is the best subject in the world")
                .build();
    }

    @Test
    @Sql(value = "/data.sql")
    @Order(1)
    public void testMethodCreateWorkOk() {
        //When
        Note actual = noteService.create(expected);

        //Then
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getUser().getUsername(), "test_user");
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    @Order(2)
    public void testThatMethodCreateThrowExceptionNoteAlreadyExists() {
        String expectedMessage = NoteMessage.noteAlreadyExists.name();
        Note note = noteService.getById(1);

        String actualMessage = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            noteService.create(note);
        }).getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);

    }

    @Test
    @Order(3)
    public void testThatMethodCreateThrowExceptionWithMessageInvalidTitle() {

        //Given
        String expectedMessage = NoteMessage.invalidTitle.name();
        Note note = Note.builder()
                .user(
                        User.builder()
                                .username("test_user")
                                .password("$2a$12$kpU9BuqBbOyUvPo89aNRReiNFFSBYQ9zfecLhsTgYLlwFrHuisYoi")
                                .build()
                )
                .title("Geo")
                .content("Geodesy is the best subject in the world")
                .build();

        //When
        String actualMessage = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            noteService.create(note);
        }).getMessage();

        //Then
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @Order(4)
    public void testThatMethodCreateThrowExceptionWithMessageInvalidContent() {

        //Given
        String expectedMessage = NoteMessage.invalidContent.name();
        Note note = Note.builder()
                .user(
                        User.builder()
                                .username("test_user")
                                .password("$2a$12$kpU9BuqBbOyUvPo89aNRReiNFFSBYQ9zfecLhsTgYLlwFrHuisYoi")
                                .build()
                )
                .title("Geodesy")
                .content("Geo")
                .build();

        //When
        String actualMessage = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            noteService.create(note);
        }).getMessage();

        //Then
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @Order(5)
    public void testThatMethodGetByIdWorkOk() {

        //When
        Note actual = noteService.getById(3L);

        //Then
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getUser().getUsername(), actual.getUser().getUsername());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    @Order(6)
    public void testThatMethodGetByIdThrowException() {

        //Given
        String expectedMessage = NoteMessage.invalidNoteId.name();

        //When
        IllegalArgumentException actual = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            noteService.getById(20L);
        });

        //Then
        Assertions.assertEquals(expectedMessage, actual.getMessage());

    }

    @Test
    @Order(7)
    public void testThatMethodUpdateDataWorkOk() {

        //Given
        Note expectedForTestingUpdate = noteService.getById(3L);
        expectedForTestingUpdate.setTitle("Mathematics");
        expectedForTestingUpdate.setContent("No mathematics is the best subject in the world!");

        //When
        noteService.update(expectedForTestingUpdate);
        Note actual = noteService.getById(3L);

        //Then
        Assertions.assertEquals(expectedForTestingUpdate.getId(), actual.getId());
        Assertions.assertEquals(expectedForTestingUpdate.getUser().getUsername(), actual.getUser().getUsername());
        Assertions.assertEquals(expectedForTestingUpdate.getTitle(), actual.getTitle());
        Assertions.assertEquals(expectedForTestingUpdate.getContent(), actual.getContent());
    }

    @Test
    @Order(8)
    public void testThatMethodUpdateThrowExceptionWithMessageInvalidNoteId() {

        //Given
        String expectedMessage = NoteMessage.invalidNoteId.name();
        Note note = new Note();

        //When
        String actualMessage = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            noteService.update(note);
        }).getMessage();

        //Then
        Assertions.assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @Order(9)
    public void testThatMethodGetUserNotesWorkOk() {

        //Given
        String username = "test_user";

        //When
        List<Note> actual = noteService.getUserNotes(username);

        //Then
        Assertions.assertEquals(3, actual.size());
    }

    @Test
    @Order(10)
    public void testThatMethodDeleteWorkOk() {
        //Given
        String username = "test_user";

        //When
        noteService.delete(expected);
        List<Note> actual = noteService.getUserNotes(username);

        //Then
        Assertions.assertFalse(actual.contains(expected));

    }
}