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
        return noteRepository.save(note);
    }

    public List<Note> getAll() {
        return noteRepository.findAll();
    }

    public Note getById(long id) {
        Optional<Note> result = noteRepository.findById(String.valueOf(id));

        if (!result.isPresent()) {
            throw new IllegalArgumentException("Note with id=" + id + " not found");
        }

        return result.get();
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

}
