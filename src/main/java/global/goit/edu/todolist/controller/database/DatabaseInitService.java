package global.goit.edu.todolist.controller.database;

import org.flywaydb.core.Flyway;

public class DatabaseInitService {

    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        "jdbc:h2:./src/main/resources/db/todolist",
                        "admin",
                        "admin"
                )
                .load();
        flyway.migrate();
    }
}