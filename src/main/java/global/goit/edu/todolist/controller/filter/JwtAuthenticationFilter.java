package global.goit.edu.todolist.controller.filter;

import global.goit.edu.todolist.model.entity.dto.login.LoginResponse;
import global.goit.edu.todolist.model.entity.message.AuthMessage;
import global.goit.edu.todolist.model.service.CookieService;
import global.goit.edu.todolist.model.service.JwtService;
import global.goit.edu.todolist.model.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.List;


public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final CookieService cookieService;
    @Value("${token.update.time}")
    private long tokenUpdateTime;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService,
                                   JwtService jwtService, CookieService cookieService) {
        super(authenticationManager);
        this.jwtService = jwtService;
        this.userService = userService;
        this.cookieService = cookieService;
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Получаем токен из cookies
        String token = cookieService.getToken(request);

        //Если токен пустой и пользователь не аутентифицирован выходим из фильтра
        if (StringUtils.isEmpty(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String username = "";

        try {
            //Если токен не пустой, достаём из токена имя пользователя
            if (StringUtils.isNotEmpty(token)) {

                username = jwtService.extractUsername(token);

                //Если имя пользователя не пустое и он не аутентифицирован загружаем с БД пользователя
                if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

                    var userDetails = userService.userDetailsService().loadUserByUsername(username);

                    /* Проверка на валидность токена (не прошло ли время действия токена),
                     * если токен валиден проводим авторизацию и аутентификацию
                     */
                    if (Boolean.TRUE.equals(jwtService.validateToken(token, userDetails))) {
                        var authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                List.of(new SimpleGrantedAuthority("USER"))
                        );
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authToken);
                        SecurityContextHolder.setContext(context);
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }
}