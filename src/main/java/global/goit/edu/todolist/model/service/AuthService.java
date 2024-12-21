package global.goit.edu.todolist.model.service;

import global.goit.edu.todolist.model.entity.dto.login.LoginRequest;
import global.goit.edu.todolist.model.entity.dto.login.LoginResponse;
import global.goit.edu.todolist.model.entity.dto.register.RegisterRequest;
import global.goit.edu.todolist.model.entity.dto.register.RegisterResponse;
import global.goit.edu.todolist.model.entity.message.AuthMessage;
import global.goit.edu.todolist.model.entity.user.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    public RegisterResponse register(RegisterRequest request) {
        String message = "";
        if (!request.getUsername().isEmpty() && !request.getPassword().isEmpty()) {
            User requestUser = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            try {
                User user = userService.create(requestUser);
                return RegisterResponse.registrationSuccess();
            } catch (IllegalArgumentException e) {
                message = e.getMessage();
            }
        }
        return RegisterResponse.registerFail(AuthMessage.valueOf(message));
    }

    public LoginResponse login(LoginRequest request,
                               HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) {

        if (!userService.isExistsByUsername(request.getUsername())) {
            return LoginResponse.loginFail(AuthMessage.invalidUsername);
        }

        User user = userService.getByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return LoginResponse.loginFail(AuthMessage.invalidPassword);
        }


        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword(),
                List.of(new SimpleGrantedAuthority("USER"))
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        String token = jwtService.generateToken(userService.getCurrentUser());

        cookieService.setExpirationTimeByZeroToToken(httpServletRequest, httpServletResponse);
        cookieService.addToken(httpServletResponse, token);

        return LoginResponse.loginSuccess(token);
    }

}
