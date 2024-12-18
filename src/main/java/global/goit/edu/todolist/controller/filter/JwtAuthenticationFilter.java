package global.goit.edu.todolist.controller.filter;

import global.goit.edu.todolist.model.service.CookieService;
import global.goit.edu.todolist.model.service.JwtService;
import global.goit.edu.todolist.model.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserService userService;

    private final CookieService cookieService;
    @Value("${token.update.time}")
    private long tokenUpdateTime;

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

        //Если токен не пустой, достаём из токена имя пользователя
        if (StringUtils.isNotEmpty(token)) {
            String username = jwtService.extractUsername(token);
            long tokenExpiration = jwtService.extractExpiration(token).getTime();

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
                            userDetails.getAuthorities()
                    );
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }

                //Если до окончания действия токена осталось меньше минуты, обновляем токен
                if ((tokenExpiration - System.currentTimeMillis()) <= tokenUpdateTime) {
                    cookieService.updateToken(request, response, jwtService.generateToken(userDetails));
                }

                //Если юзер аутентифицирован но до окончания действия токена осталось меньше минуты, обновляем токен
            } else if (StringUtils.isNotEmpty(username) && ((tokenExpiration - System.currentTimeMillis()) <= tokenUpdateTime)) {
                cookieService.updateToken(request, response, jwtService.generateToken(userService.getCurrentUser()));
            }
            //Если токен пустой но юзер аутентифицирован, добавляем токен в cookies
        } else if (StringUtils.isEmpty(token) && SecurityContextHolder.getContext().getAuthentication() != null) {
            cookieService.addToken(response, jwtService.generateToken(userService.getCurrentUser()));
        }
        filterChain.doFilter(request, response);
    }
}