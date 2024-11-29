package global.goit.edu.todolist.model.service;

import global.goit.edu.todolist.TodoListTestBaseClass;
import global.goit.edu.todolist.model.note.Note;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
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
        Note actual = noteService.save(expected);
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
        Note expectedForTestingUpdate = noteService.save(expected);
        expectedForTestingUpdate.setTitle("Mathematics");
        expectedForTestingUpdate.setContent("No mathematics is the best subject in the world!");

        //When
        noteService.save(expectedForTestingUpdate);
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

        //When
        List<Note> actual = noteService.getAll();

        //Then
        Assertions.assertEquals(6, actual.size());
    }

    @Test
    @Order(6)
    public void testThatMethodDeleteWorkOk() {

        //When
        noteService.delete(expected);
        List<Note> actual = noteService.getAll();

        //Then
        Assertions.assertFalse(actual.contains(expected));

    }
}