package global.goit.edu.todolist.model.service;

import global.goit.edu.todolist.controller.repository.NoteRepository;
import global.goit.edu.todolist.model.entity.note.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public Note save(Note note) {
        if (note == null){
            throw new IllegalArgumentException("Note is null");
        }
        return noteRepository.save(note);
    }

    public Note create(Note note) {

        boolean isNoteExists = noteRepository.existsById(note.getId());

        if (isNoteExists) {
            throw new IllegalArgumentException("Note with id=" + note.getId() + " is created");
        }

        return noteRepository.save(note);
    }

    public List<Note> getAll() {
        return noteRepository.findAll();
    }

    public Note getById(long id) {

        if (!noteRepository.existsById(id)) {
            throw new IllegalArgumentException("Note with id=" + id + " not found");
        }

        return noteRepository.findById(id).get();
    }

    public void delete(Note note) {

        if (!noteRepository.existsById(note.getId())) {
            throw new IllegalArgumentException("Note with id=" + note.getId() + " not found");
        }

        noteRepository.delete(note);
    }

}
