package global.goit.edu.todolist;


import com.google.gson.reflect.TypeToken;
import global.goit.edu.todolist.note.Note;
import global.goit.edu.todolist.note.NoteService;
import global.goit.edu.todolist.reader.FileReader;
import global.goit.edu.todolist.reader.JsonFileReader;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Tests {

    public static void main(String[] args) {
/*        Note noteGeodesy = Note.builder()
                .id(1L)
                .title("Geodesy")
                .content("Geodesy is the best subject in the world")
                .build();
        Note noteMathematics = Note.builder()
                .id(1L)
                .title("Mathematics")
                .content("No mathematics is the best subject in the world!")
                .build();
        Note noteGeo = Note.builder()
                .id(2L)
                .title("Geo")
                .content("555")
                .build();

        NoteService noteService = new NoteService();

        noteService.add(noteGeodesy);
        noteService.add(noteMathematics);
        noteService.update(noteGeo);
        noteService.getAll().forEach(System.out::println);*/

        FileReader jsonFileReader = new JsonFileReader();

        ArrayList<Note> read = jsonFileReader.read(
                "./src/main/resources/json/population.json",
                new TypeToken<ArrayList<Note>>() {}.getType()
        );

        for (Note note : read) {
            System.out.println(note);
        }
    }
}
