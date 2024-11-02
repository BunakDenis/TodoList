package global.goit.edu.todolist.note;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class NoteServiceTests {

    private NoteService noteService;

    @BeforeEach
    public void BeforeEach() {
        noteService = new NoteService();
    }

    @Test
    public void testThatMethodAddWorkOk() {
        //Given
        Note expected = Note.builder()
                .id(1L)
                .title("Geodesy")
                .content("Geodesy is the best subject in the world")
                .build();

        //Then
        Note actual = noteService.add(expected);

        //When
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    public void testThatMethodGetByIdWorkOk() {

        //Given
        Note expected = Note.builder()
                .id(1L)
                .title("Geodesy")
                .content("Geodesy is the best subject in the world")
                .build();
        noteService.add(expected);

        //When
        Note actual = noteService.getById(1L);

        //Then
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    public void testThatMethodGetByIdThrowException() {

        //Given
        String expectedMessage = "Note with id=1 not found.";

        //When
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> noteService.getById(1L)
        );

        //Then
        Assertions.assertEquals(expectedMessage, exception.getMessage());

    }

    @Test
    public void testThatMethodUpdateWorkOk() {

        //Given
        Note note = Note.builder()
                .title("Geodesy")
                .content("Geodesy is the best subject in the world")
                .build();
        Note expected = noteService.add(note);
        expected.setTitle("Mathematics");
        expected.setContent("No mathematics is the best subject in the world!");

        //When
        noteService.update(expected);
        Note actual = noteService.getById(1L);

        //Then
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    public void testThatMethodGetAllWorkOk() {

        //Given
        List<Note> expected = new ArrayList<>();
        Note noteGeodesy = Note.builder()
                .id(1L)
                .title("Geodesy")
                .content("Geodesy is the best subject in the world")
                .build();
        Note noteMathematics = Note.builder()
                .id(2L)
                .title("Mathematics")
                .content("No mathematics is the best subject in the world!")
                .build();
        expected.add(noteGeodesy);
        expected.add(noteMathematics);

        //When
        noteService.add(noteGeodesy);
        noteService.add(noteMathematics);
        List<Note> actual = noteService.getAll();

        //Then
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).getId(), actual.get(i).getId());
            Assertions.assertEquals(expected.get(i).getTitle(), actual.get(i).getTitle());
            Assertions.assertEquals(expected.get(i).getContent(), actual.get(i).getContent());
        }
    }

    @Test
    public void testThatMethodDeleteWorkOk() {

        //Given
        Note note = Note.builder()
                .title("Geodesy")
                .content("Geodesy is the best subject in the world")
                .build();
        Note expected = noteService.add(note);

        //When
        noteService.delete(note.getId());
        List<Note> actual = noteService.getAll();

        //Then
        Assertions.assertFalse(actual.contains(expected));

    }

    @Test
    public void testThatMethodDeleteThrowException() {

        //Given
        String expectedMessage = "Note with id=1 not found.";

        //When
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> noteService.delete(1L)
        );

        //Then
        Assertions.assertEquals(expectedMessage, exception.getMessage());

    }
}