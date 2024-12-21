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
        User currentUser = new User();
        Note note = new Note();
        try {
            currentUser = userService.getCurrentUser();
        } catch (Exception e) {
            return NoteResponse.failed(Operation.CREATE, NoteMessage.valueOf(e.getMessage()));
        }

        try {
            note = noteService.create(Note.builder()
                    .user(currentUser)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .build()
            );
        } catch (IllegalArgumentException e) {
            return NoteResponse.failed(Operation.CREATE, NoteMessage.valueOf(e.getMessage()));
        }

        return NoteResponse.success(Operation.CREATE, List.of(note));
    }

    @GetMapping("/list")
    public NoteResponse getAllNotes() {
        User currentUser = new User();
        List<Note> result;

        try {
            currentUser = userService.getCurrentUser();
        } catch (Exception e) {
            return NoteResponse.failed(Operation.READ, NoteMessage.valueOf(e.getMessage()));
        }

        try {
            result = noteService.getUserNotes(currentUser.getUsername());
        } catch (IllegalArgumentException e) {
            return NoteResponse.failed(Operation.READ, NoteMessage.invalidUsername);
        }

        return NoteResponse.success(Operation.READ, result);
    }

    @GetMapping("/get/{id}")
    public NoteResponse getNote(@PathVariable long id) {
        Note note = new Note();

        try {
            note = noteService.findNoteByIdInCurrentUser(id);
        } catch (IllegalArgumentException e) {
            return NoteResponse.failed(Operation.READ, NoteMessage.valueOf(e.getMessage()));
        }
        return NoteResponse.success(Operation.READ, List.of(note));
    }

    @PostMapping("/edit")
    public NoteResponse editNote(@RequestBody NoteRequest request) {
        Note note = new Note();

        try {
            note = noteService.findNoteByIdInCurrentUser(request.getId());
        } catch (IllegalArgumentException e) {
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

        try {
            note = noteService.findNoteByIdInCurrentUser(id);
        } catch (IllegalArgumentException e) {
            return NoteResponse.failed(Operation.DELETE, NoteMessage.invalidNoteId);
        }

        noteService.delete(note);

        return NoteResponse.success(Operation.DELETE, List.of(note));
    }
}