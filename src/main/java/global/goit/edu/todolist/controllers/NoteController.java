package global.goit.edu.todolist.controllers;

import global.goit.edu.todolist.note.Note;
import global.goit.edu.todolist.note.NoteService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;


    /*
    POST /note/delete - видалити нотатку по ID. Після видалення нотатки відбувається редирект на /note/list
    --- Необхідно в html шаблоні виправити адресу post запиту, наразі в метод delete приходить адреса без id

    GET /note/edit?id=xxx - сторінка редагування нотатку (відкривається по натисненню на кнопку Редагувати на списку нотаток).
        --- Необхідно в html шаблоні виправити адресу get запиту, наразі в метод delete приходить адреса без id

    POST /note/edit - сюди відправляється запит на редагування нотатки. Після збереження оновленого контенту нотатки відбувається редирект на /note/list
    --- Необхідно написати метод передачі заповнених данних з форми в json формат та відправка post запиту з json файлом в body
     */

    @PostConstruct
    public void init() {
        noteService.add(
                Note.builder()
                        .id(1L)
                        .title("Geodesy")
                        .content("Geodesy is the best subject in the world")
                        .build()
        );
        noteService.add(
                Note.builder()
                        .id(2L)
                        .title("Mathematics")
                        .content("No mathematics is the best subject in the world!")
                        .build()
        );
    }

    @GetMapping("/list")
    public ModelAndView getList() {
        ModelAndView result = new ModelAndView("note/list");

        List<Note> notes = noteService.getAll();
        notes.forEach(System.out::println);
        result.addObject("notes", notes);
        return result;
    }

    @GetMapping("/getById/{id}")
    public ModelAndView getById(@PathVariable long id) {
        Note findedNote = noteService.getById(id);

        if (findedNote != null) {
            ModelAndView result = new ModelAndView("note/get-by-id");
            result.addObject("note", findedNote);
            return result;
        }

        ModelAndView result = new ModelAndView("note/get-by-id-not-found");
        result.addObject(
                "note",
                "Note with id=" + id + " not found");

        return result;
    }

    @PostMapping("/delete/{id}")
    public RedirectView deleteById(@PathVariable long id) {
        noteService.delete(id);
        return new RedirectView("note/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable long id) {
        ModelAndView result = new ModelAndView("note/edit");
        Note note = noteService.getById(id);

        result.addObject("note", note);
        return result;
    }

}