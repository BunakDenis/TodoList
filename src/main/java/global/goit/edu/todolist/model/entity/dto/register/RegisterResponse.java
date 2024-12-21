package global.goit.edu.todolist.model.entity.dto.register;

import global.goit.edu.todolist.model.entity.message.AuthMessage;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class RegisterResponse {

    private AuthMessage message;

    public static RegisterResponse registrationSuccess() {
        return builder().message(AuthMessage.ok).build();
    }

    public static RegisterResponse registerFail(AuthMessage authMessage) {
        return builder().message(authMessage).build();
    }

}
