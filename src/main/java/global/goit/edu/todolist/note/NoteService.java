package global.goit.edu.todolist.note;

import global.goit.edu.todolist.repository.NoteRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public Note save(Note note) {
        note.setId(0);
        return noteRepository.save(note);
    }

    public List<Note> getAll() {
        return noteRepository.findAll();
    }

    public Note getById(long id) {
        Optional<Note> result = noteRepository.findById(String.valueOf(id));

        return result.get();
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }


}
