package global.goit.edu.todolist.controller;

import global.goit.edu.todolist.model.entity.dto.login.LoginRequest;
import global.goit.edu.todolist.model.entity.dto.login.LoginResponse;
import global.goit.edu.todolist.model.entity.dto.register.RegisterRequest;
import global.goit.edu.todolist.model.entity.dto.register.RegisterResponse;
import global.goit.edu.todolist.model.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request,
                               HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) {
        return authService.login(request, httpServletRequest, httpServletResponse);
    }

    @PostMapping("/reg")
    public RegisterResponse registration(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

}
