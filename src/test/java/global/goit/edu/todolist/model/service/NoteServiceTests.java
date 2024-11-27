package global.goit.edu.todolist.model.service;

import global.goit.edu.todolist.controller.repository.NoteRepository;
import global.goit.edu.todolist.model.note.Note;
import global.goit.edu.todolist.model.service.NoteService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@ContextConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteServiceTests {
    @Autowired
    private NoteRepository noteRepository;
    @SpyBean
    private NoteService noteService;

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
        Note actual = noteRepository.save(expected);
        System.out.println("call method find all in Save test");
        noteRepository.findAll().forEach(System.out::println);
        //Then
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    @Order(2)
    public void testThatMethodGetByIdWorkOk() {

        //When
        noteRepository.save(expected);
        System.out.println("call method find all in GetById test");
        noteRepository.findAll().forEach(System.out::println);
        Optional<Note> find = noteRepository.findById("2");
        Note actual = find.get();

        //Then
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    @Order(3)
    public void testThatMethodGetByIdThrowException() {

        //Given
        String expectedMessage = "Note with id=20 not found";
        NoteRepository mockRepository = Mockito.mock(NoteRepository.class);

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
        Note expectedForTestingUpdate = noteRepository.save(expected);
        //expectedForTextingUpdate.setTitle("Mathematics");
        System.out.println("call method find all in Update test");
        noteRepository.findAll().forEach(System.out::println);
        expectedForTestingUpdate.setTitle("Mathematics");
        //expectedForTextingUpdate.setContent("No mathematics is the best subject in the world!");
        expectedForTestingUpdate.setContent("No mathematics is the best subject in the world!");

        //When
        noteRepository.save(expectedForTestingUpdate);
        Note actual = noteRepository.getById("3");
        System.out.println("actual = " + actual);

        //Then
        Assertions.assertEquals(expectedForTestingUpdate.getId(), actual.getId());
        Assertions.assertEquals(expectedForTestingUpdate.getTitle(), actual.getTitle());
        Assertions.assertEquals(expectedForTestingUpdate.getContent(), actual.getContent());
    }

    @Test
    @Order(5)
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
        System.out.println("noteRepository.findAll().forEach(System.out::println)");
        noteRepository.findAll().forEach(System.out::println);

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
    @Order(6)
    public void testThatMethodDeleteWorkOk() {

        //When
        //noteRepository.save(expected);
        //expected.setId(6L);
        noteRepository.delete(expected);
        List<Note> actual = noteRepository.findAll();

        //Then
        Assertions.assertFalse(actual.contains(expected));

    }
}