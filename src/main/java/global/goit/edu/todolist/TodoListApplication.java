package global.goit.edu.todolist;

import global.goit.edu.todolist.note.NoteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TodoListApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TodoListApplication.class, args);
	}
}