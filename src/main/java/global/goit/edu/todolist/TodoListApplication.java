package global.goit.edu.todolist;

import global.goit.edu.todolist.note.NoteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoListApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteService.class, args);
	}

}
