package global.goit.edu.todolist;


import global.goit.edu.todolist.model.service.JwtService;
import global.goit.edu.todolist.model.service.NoteService;
import global.goit.edu.todolist.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class TodoListTestBaseClass {
    @Autowired
    protected NoteService noteService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected JwtService jwtService;
}