package global.goit.edu.todolist.model.request;


import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class SignInRequest {

    private String username;
    private String password;

}
