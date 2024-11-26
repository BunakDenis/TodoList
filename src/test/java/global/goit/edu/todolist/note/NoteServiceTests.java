package global.goit.edu.todolist.note;

import global.goit.edu.todolist.repository.NoteRepository;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteServiceTests {
    @Autowired
    private NoteRepository noteRepository;

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
        //Then
        Note actual = noteRepository.save(expected);

        //When
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    @Order(2)
    public void testThatMethodGetByIdWorkOk() {

        noteRepository.save(expected);

        //When
        Optional<Note> find = noteRepository.findById("2");
        Note actual = find.get();

        //Then
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    @Order(3)
    public void testThatMethodSaveUpdateData() {

        Note expectedForTextingUpdate = noteRepository.save(expected);
        expectedForTextingUpdate.setTitle("Mathematics");
        expectedForTextingUpdate.setContent("No mathematics is the best subject in the world!");

        //When
        noteRepository.save(expectedForTextingUpdate);
        Note actual = noteRepository.getById("3");

        //Then
        Assertions.assertEquals(expectedForTextingUpdate.getId(), actual.getId());
        Assertions.assertEquals(expectedForTextingUpdate.getTitle(), actual.getTitle());
        Assertions.assertEquals(expectedForTextingUpdate.getContent(), actual.getContent());
    }

    @Test
    @Order(4)
    public void testThatMethodGetAllWorkOk() {

        //Given
        List<Note> expectedList = new ArrayList<>();
        Note noteMathematics = Note.builder()
                .id(5L)
                .title("Mathematics")
                .content("No mathematics is the best subject in the world!")
                .build();
        expected.setId(4L);
        expectedList.add(expected);
        expectedList.add(noteMathematics);

        //When
        noteRepository.save(expected);
        noteRepository.save(noteMathematics);
        List<Note> actual = noteRepository.findAll();

        //Then
        for (int i = 0; i < expectedList.size(); i++) {
            Assertions.assertEquals(expectedList.get(i).getId(), actual.get(i).getId());
            Assertions.assertEquals(expectedList.get(i).getTitle(), actual.get(i).getTitle());
            Assertions.assertEquals(expectedList.get(i).getContent(), actual.get(i).getContent());
        }
    }

    @Test
    @Order(5)
    public void testThatMethodDeleteWorkOk() {

        //When
        noteRepository.save(expected);
        expected.setId(6L);
        noteRepository.delete(expected);
        List<Note> actual = noteRepository.findAll();

        //Then
        Assertions.assertFalse(actual.contains(expected));

    }
}