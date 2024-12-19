package global.goit.edu.todolist.model.entity.dto.note;

import global.goit.edu.todolist.model.entity.dto.oparation.Operation;
import global.goit.edu.todolist.model.entity.message.NoteMessage;
import global.goit.edu.todolist.model.entity.note.Note;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class NoteResponse {

    private Operation operation;
    private NoteMessage message;
    private List<Note> notes;

    public static NoteResponse success(Operation operation, List<Note> notes) {
        return NoteResponse.builder().operation(operation).message(NoteMessage.OK).notes(notes).build();
    }

    public static NoteResponse failed(Operation operation, NoteMessage error) {
        return NoteResponse.builder().operation(operation).message(error).build();
    }

}
