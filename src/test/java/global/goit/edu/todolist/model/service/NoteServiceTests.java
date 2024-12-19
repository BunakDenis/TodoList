package global.goit.edu.todolist.model.service;

import global.goit.edu.todolist.TodoListTestBaseClass;
import global.goit.edu.todolist.model.entity.note.Note;
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
                .id(1L)
                .title("Geodesy")
                .content("Geodesy is the best subject in the world")
                .build();
    }

    @Test
    @Order(1)
    public void testMethodSaveWorkOk() {
        //When
        Note actual = noteService.update(expected);
        //Then
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    @Order(2)
    public void testThatMethodGetByIdWorkOk() {

        //When
        Note actual = noteService.getById(1L);

        //Then
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    @Order(3)
    public void testThatMethodGetByIdThrowException() {

        //Given
        String expectedMessage = "Note with id=20 not found";

        //When
        IllegalArgumentException actual = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            noteService.getById(20L);
        });

        //Then
        Assertions.assertEquals(expectedMessage, actual.getMessage());

    }

    @Test
    @Order(4)
    public void testThatMethodSaveUpdateData() {

        //Given
        Note expectedForTestingUpdate = noteService.update(expected);
        expectedForTestingUpdate.setTitle("Mathematics");
        expectedForTestingUpdate.setContent("No mathematics is the best subject in the world!");

        //When
        noteService.update(expectedForTestingUpdate);
        Note actual = noteService.getById(1L);

        //Then
        Assertions.assertEquals(expectedForTestingUpdate.getId(), actual.getId());
        Assertions.assertEquals(expectedForTestingUpdate.getTitle(), actual.getTitle());
        Assertions.assertEquals(expectedForTestingUpdate.getContent(), actual.getContent());
    }

    @Test
    @Sql(value = "/data.sql")
    @Order(5)
    public void testThatMethodGetAllWorkOk() {

        //Given
        String username = "user";

        //When
        List<Note> actual = noteService.getUserNotes(username);

        //Then
        Assertions.assertEquals(6, actual.size());
    }

    @Test
    @Order(6)
    public void testThatMethodDeleteWorkOk() {
        //Given
        String username = "user";

        //When
        noteService.delete(expected);
        List<Note> actual = noteService.getUserNotes(username);

        //Then
        Assertions.assertFalse(actual.contains(expected));

    }
}