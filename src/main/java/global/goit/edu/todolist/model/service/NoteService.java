package global.goit.edu.todolist.model.service;

import global.goit.edu.todolist.controller.repository.NoteRepository;
import global.goit.edu.todolist.model.entity.message.NoteMessage;
import global.goit.edu.todolist.model.entity.note.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;

    public Note update(Note note) {

        noteValidation(note);

        return noteRepository.save(note);
    }

    public Note create(Note note) {

        if (noteRepository.existsByTitle(note.getTitle()) && noteRepository.existsByContent(note.getContent())) {
            throw new IllegalArgumentException(NoteMessage.noteAlreadyExists.name());
        }

        noteValidation(note);

        return noteRepository.save(note);
    }

    public List<Note> getUserNotes(String username) {
        List<Note> userNotes = noteRepository.getUserNotes(username);

        if (Objects.isNull(userNotes)) {
            throw new IllegalArgumentException(NoteMessage.invalidUsername.name());
        }

        return userNotes;
    }

    public Note getById(long id) {

        if (!noteRepository.existsById(id)) {
            throw new IllegalArgumentException(NoteMessage.invalidNoteId.name());
        }

        return noteRepository.findById(id).get();
    }

    public Note findNoteByIdInCurrentUser(long id) {
        Note result = new Note();
        for (Note n : userService.getCurrentUser().getNotes()) {

            if (n.getId() == id) {
                result = n;
                break;
            }
        }

        if (Objects.isNull(result)) {
            throw new IllegalArgumentException(NoteMessage.invalidNoteId.name());
        }

        return result;
    }

    public void delete(Note note) {

        if (!noteRepository.existsById(note.getId())) {
            throw new IllegalArgumentException(NoteMessage.invalidNoteId.name());
        }
        noteRepository.delete(note);
    }

    public List<Note> getAll() {
        return noteRepository.findAll();
    }

    private void noteValidation(Note note) {

        if (Objects.isNull(note.getUser())
                && Objects.isNull(note.getTitle())
                && Objects.isNull(note.getContent())) {

            throw new IllegalArgumentException(NoteMessage.invalidNoteId.name());
        }

        if (note.getTitle().length() < 5) {
            throw new IllegalArgumentException(NoteMessage.invalidTitle.name());
        }

        if (note.getContent().length() < 5) {
            throw new IllegalArgumentException(NoteMessage.invalidContent.name());
        }
    }

}
