package global.goit.edu.todolist.model.service;

import global.goit.edu.todolist.controller.repository.NoteRepository;
import global.goit.edu.todolist.model.entity.note.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;

    public Note update(Note note) {
        if (note == null){
            throw new IllegalArgumentException("Note is null");
        }
        return noteRepository.save(note);
    }

    public Note create(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> getUserNotes(String username) {
        List<Note> userNotes = noteRepository.getUserNotes(username);
        return userNotes;
    }

    public Note getById(long id) {

        if (!noteRepository.existsById(id)) {
            throw new IllegalArgumentException("Note with id=" + id + " not found");
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
        return result;
    }

    public void delete(Note note) {

        if (!noteRepository.existsById(note.getId())) {
            throw new IllegalArgumentException("Note with id=" + note.getId() + " not found");
        }

        noteRepository.delete(note);
    }

}
