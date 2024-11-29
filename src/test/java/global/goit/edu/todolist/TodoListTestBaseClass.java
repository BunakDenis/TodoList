package global.goit.edu.todolist;

import global.goit.edu.todolist.controller.NoteController;
import global.goit.edu.todolist.controller.repository.NoteRepository;
import global.goit.edu.todolist.model.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class TodoListTestBaseClass {

    @Autowired
    protected NoteController noteController;
    @Autowired
    protected NoteService noteService;
    @Autowired
    protected NoteRepository noteRepository;

}
