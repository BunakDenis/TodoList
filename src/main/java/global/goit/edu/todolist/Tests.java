package global.goit.edu.todolist;


import global.goit.edu.todolist.note.Note;
import global.goit.edu.todolist.note.NoteService;

import java.util.List;

public class Tests {

    public static void main(String[] args) {
        Note noteGeodesy = Note.builder()
                .id(1L)
                .title("Geodesy")
                .content("Geodesy is the best subject in the world")
                .build();
        Note noteMathematics = Note.builder()
                .id(1L)
                .title("Mathematics")
                .content("No mathematics is the best subject in the world!")
                .build();

        NoteService noteService = new NoteService();

        noteService.add(noteGeodesy);
        noteService.add(noteMathematics);
        noteService.getAll().forEach(System.out::println);

    }
}
