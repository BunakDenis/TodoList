package global.goit.edu.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TodoListApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TodoListApplication.class, args);
	}
}