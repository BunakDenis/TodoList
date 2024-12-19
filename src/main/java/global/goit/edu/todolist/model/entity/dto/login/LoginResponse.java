package global.goit.edu.todolist.model.entity.dto.login;

import global.goit.edu.todolist.model.entity.message.AuthMessage;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class LoginResponse {

    private AuthMessage message;
    private String authToken;

    public static LoginResponse loginSuccess(String authToken) {
        return builder().message(AuthMessage.ok).authToken(authToken).build();
    }

    public static LoginResponse loginFail(AuthMessage authMessage) {
        return builder().message(authMessage).build();
    }
}
