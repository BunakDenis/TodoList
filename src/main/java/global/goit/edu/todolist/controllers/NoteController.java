package global.goit.edu.todolist.controllers;

import com.google.gson.reflect.TypeToken;
import global.goit.edu.todolist.note.Note;
import global.goit.edu.todolist.note.NoteService;
import global.goit.edu.todolist.reader.FileReader;
import global.goit.edu.todolist.reader.JsonFileReader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostConstruct
    public void init() {
        FileReader jsonFileReader = new JsonFileReader();

        ArrayList<Note> read = jsonFileReader.read(
                "./src/main/resources/json/population.json",
                new TypeToken<ArrayList<Note>>() {}.getType()
        );

        for (Note note : read) {
            noteService.add(note);
        }
    }

    @GetMapping("/list")
    public ModelAndView getList() {
        ModelAndView result = new ModelAndView("note/list");

        List<Note> notes = noteService.getAll();
        result.addObject("notes", notes);
        return result;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteById(@PathVariable long id) {
        noteService.delete(id);

        RedirectView redirectView = new RedirectView("/note/list");
        return new ModelAndView(redirectView);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editNotePage(@PathVariable long id) {
        ModelAndView result = new ModelAndView("note/edit");
        Note note = noteService.getById(id);

        result.addObject("note", note);
        return result;
    }

    @PostMapping("/edit")
    public ModelAndView editNote(@ModelAttribute("note") Note note) {

        if (noteService.findNoteById(note.getId()) != -1) {
            noteService.update(note);
        }

        RedirectView redirectView = new RedirectView("/note/list");

        return new ModelAndView(redirectView);
    }

    @PostMapping("/add")
    public ModelAndView addNote(@ModelAttribute("note") Note note) {

        if (note != null) {
            noteService.add(note);
        }

        RedirectView redirectView = new RedirectView("/note/list");

        return new ModelAndView(redirectView);
    }
}