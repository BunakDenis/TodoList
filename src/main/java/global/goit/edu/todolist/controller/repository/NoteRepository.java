package global.goit.edu.todolist.controller.repository;

import global.goit.edu.todolist.model.entity.note.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM note n WHERE n.user_id = :username")
    List<Note> getUserNotes(String username);
}
