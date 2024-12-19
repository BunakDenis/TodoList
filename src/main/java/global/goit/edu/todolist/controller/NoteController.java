package global.goit.edu.todolist.controller;


import global.goit.edu.todolist.model.entity.dto.note.NoteRequest;
import global.goit.edu.todolist.model.entity.dto.note.NoteResponse;
import global.goit.edu.todolist.model.entity.dto.oparation.Operation;
import global.goit.edu.todolist.model.entity.message.NoteMessage;
import global.goit.edu.todolist.model.entity.note.Note;
import global.goit.edu.todolist.model.entity.user.User;
import global.goit.edu.todolist.model.service.NoteService;
import global.goit.edu.todolist.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    @Value("${home.url}")
    private String homeUrl;

    @PostMapping("/add")
    public NoteResponse addNote(@RequestBody NoteRequest request) {

        User currentUser = userService.getCurrentUser();

        Note note = noteService.create(Note.builder()
                .user(currentUser)
                .title(request.getTitle())
                .content(request.getContent())
                .build()
        );

        return NoteResponse.success(Operation.CREATE, List.of(note));
    }

    @GetMapping("/list")
    public NoteResponse getAllNotes() {
        User currentUser = userService.getCurrentUser();

        List<Note> userNotes = currentUser.getNotes();

        return NoteResponse.success(Operation.READ, userNotes);
    }

    @GetMapping("/get/{id}")
    public NoteResponse getNotePage(@PathVariable long id) {
        Note note = new Note();

        note = noteService.findNoteByIdInCurrentUser(id);

        if (Objects.isNull(note)) {
            return NoteResponse.failed(Operation.READ, NoteMessage.invalidNoteId);
        }
        return NoteResponse.success(Operation.READ, List.of(note));
    }

    @PostMapping("/edit")
    public NoteResponse editNote(@RequestBody NoteRequest request) {
        Note note = new Note();

        note = noteService.findNoteByIdInCurrentUser(request.getId());

        if (Objects.isNull(note)) {
            return NoteResponse.failed(Operation.UPDATE, NoteMessage.invalidNoteId);
        }

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        Note result = noteService.update(note);

        return NoteResponse.success(Operation.UPDATE, List.of(result));
    }

    @PostMapping("/delete/{id}")
    public NoteResponse deleteById(@PathVariable long id) {
        Note note = new Note();

        note = noteService.findNoteByIdInCurrentUser(id);

        if (Objects.isNull(note)) {
            return NoteResponse.failed(Operation.DELETE, NoteMessage.invalidNoteId);
        }

        noteService.delete(note);

        return NoteResponse.success(Operation.DELETE, List.of(note));
    }
}