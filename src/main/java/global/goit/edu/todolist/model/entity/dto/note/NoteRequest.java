package global.goit.edu.todolist.model.entity.dto.note;

import lombok.Data;

@Data
public class NoteRequest {

    private long id;
    private String username;
    private String title;
    private String content;

}
