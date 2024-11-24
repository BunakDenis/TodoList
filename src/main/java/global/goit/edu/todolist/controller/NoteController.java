package global.goit.edu.todolist.controller;

import global.goit.edu.todolist.note.Note;
import global.goit.edu.todolist.note.NoteService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostConstruct
    public void init() {
        //DatabaseInitService.main(null);
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
        Note note = noteService.getById(id);
        noteService.delete(note);

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

        if (noteService.getById(note.getId()) != null) {
            noteService.save(note);
        }

        RedirectView redirectView = new RedirectView("/note/list");

        return new ModelAndView(redirectView);
    }

    @PostMapping("/add")
    public ModelAndView addNote(@ModelAttribute("note") Note note) {
        System.out.println("note = " + note);
        if (note != null) {
            noteService.save(note);
        }

        RedirectView redirectView = new RedirectView("/note/list");

        return new ModelAndView(redirectView);
    }
}