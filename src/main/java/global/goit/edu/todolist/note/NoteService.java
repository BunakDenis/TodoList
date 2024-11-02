package global.goit.edu.todolist.note;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.copy;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final List<Note> notesDb = new ArrayList<>();
    private final AtomicLong id = new AtomicLong();

    private long generateId() {
        long result = id.addAndGet(1L);
        return result;
    }

    public Note add(Note note) {
        note.setId(generateId());
        notesDb.add(note);
        return note;
    }

    public Note getById(long id) {
        Note result = new Note();

        int noteIndexInDb = findNoteById(id);

        if (noteIndexInDb == -1) {
            throw new IllegalArgumentException("Note with id=" + id + " not found.");
        }

        result = notesDb.get(noteIndexInDb);

        return result;
    }

    private int findNoteById(long id) {
        int result = -1;

        for (int i = 0; i < notesDb.size(); i++) {
            if (notesDb.get(i).getId() == id) {
                result = i;
                break;
            }
        }
        return result;
    }

    public void update(Note note) {

        if (notesDb.contains(note)) {

            notesDb.forEach(
                    n -> {
                        if (n.equals(note)) {
                            n.setTitle(note.getTitle());
                            n.setContent(note.getContent());
                        }
                    }
            );
        }
    }

    public List<Note> getAll() {
        List<Note> result = List.copyOf(notesDb);
        return result;
    }

    public void delete(long id) {

        int noteIdInDb = findNoteById(id);

        if (noteIdInDb == -1) {
            throw new IllegalArgumentException("Note with id=" + id + " not found.");
        }
        notesDb.remove(noteIdInDb);
    }
}
