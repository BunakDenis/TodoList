package global.goit.edu.todolist.controller;

import global.goit.edu.todolist.controller.repository.UserRepository;
import global.goit.edu.todolist.model.entity.user.User;
import global.goit.edu.todolist.model.request.SignInRequest;
import global.goit.edu.todolist.model.service.AuthenticationService;
import global.goit.edu.todolist.model.service.CookieService;
import global.goit.edu.todolist.model.service.JwtService;
import global.goit.edu.todolist.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final CookieService cookieService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping("/signin")
    public ModelAndView signInPage() {
        System.out.println("In get method");
        return new ModelAndView("note/signin");
    }

    @PostMapping("/signin")
    public String signIn(@RequestBody SignInRequest signInRequest,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        System.out.println("In post method");
        System.out.println("user = " + signInRequest.toString());

        String token = authenticationService.signIn(signInRequest);

        cookieService.addTokenToCookie(response, token);

        return "note/list";
    }
}