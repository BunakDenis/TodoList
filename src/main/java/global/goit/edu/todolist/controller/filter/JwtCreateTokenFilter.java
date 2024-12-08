package global.goit.edu.todolist.controller.filter;

import global.goit.edu.todolist.model.entity.user.User;
import global.goit.edu.todolist.model.service.CookieService;
import global.goit.edu.todolist.model.service.JwtService;
import global.goit.edu.todolist.model.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Data
@Component
@RequiredArgsConstructor
@WebFilter("/note/**")
@Order(1)
public class JwtCreateTokenFilter implements Filter {

    private final JwtService jwtService;

    private final UserService userService;

    private final CookieService cookieService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Create token filter");

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Optional<User> currentUser = Optional.ofNullable(userService.getCurrentUser());
        System.out.println(currentUser);

        // Получаем токен из заголовка
        String token = cookieService.getToken(httpServletRequest.getCookies());

        if (currentUser.isPresent()) {
            User user = currentUser.get();
            if (StringUtils.isEmpty(token)) {
                System.out.println("Create token filter - create token");
                String jwt = jwtService.generateToken(user);

                Cookie cookie = new Cookie(JwtAuthenticationFilter.HEADER_NAME, jwt);
                cookie.setMaxAge(JwtAuthenticationFilter.TOKEN_EXPIRATION);
                cookie.setSecure(true);
                cookie.setHttpOnly(true);

                httpServletResponse.addCookie(cookie);
                //chain.doFilter(httpServletRequest, httpServletResponse);
            }
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }
}