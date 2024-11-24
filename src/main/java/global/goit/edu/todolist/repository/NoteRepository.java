package global.goit.edu.todolist.repository;

import global.goit.edu.todolist.note.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {
}
