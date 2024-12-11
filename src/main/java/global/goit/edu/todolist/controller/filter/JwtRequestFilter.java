package global.goit.edu.todolist.controller.filter;

import global.goit.edu.todolist.controller.repository.UserRepository;
import global.goit.edu.todolist.model.entity.user.User;
import global.goit.edu.todolist.model.request.SignInRequest;
import global.goit.edu.todolist.model.service.AuthenticationService;
import global.goit.edu.todolist.model.service.CookieService;
import global.goit.edu.todolist.model.service.JwtService;
import global.goit.edu.todolist.model.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;

    private final UserRepository repository;

    private final JwtService jwtService;

    private final CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

/*      String paramUserName = request.getParameter("username");
        String paramPassword = "{noop}" + request.getParameter("password");

        System.out.println("paramUserName = " + paramUserName);
        System.out.println("repository.existsByUsername(paramUserName) = " + repository.existsByUsername(paramUserName));

        if (repository.existsByUsername(paramUserName)) {
            User user = userService.getByUsername(paramUserName);

            String userParamPassword = user.getPassword();
            System.out.println("userParamPassword = " + userParamPassword);

            if (user.getUsername().equals(paramUserName) && userParamPassword.equals(paramPassword)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                System.out.println("context = " + context);
                SecurityContextHolder.setContext(context);

                String token = jwtService.generateToken(user);
                cookieService.addTokenToCookie(response, token);

                System.out.println("SecurityContextHolder.getContext().getAuthentication().isAuthenticated() = " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
            }
        }*/

        //Получаем токен из cookie
        String tokenFromCookie = cookieService.getToken(request.getCookies());
        System.out.println("tokenFromCookie = " + tokenFromCookie);
        if (StringUtils.isEmpty(tokenFromCookie)) {
            chain.doFilter(request, response);
            return;
        }

        // Получаем имя пользователя из токена
        String username = "";

        try {
            username = jwtService.extractUserName(tokenFromCookie);
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            cookieService.setCookieAuthorizationMaxAgeZero(request, response);
            chain.doFilter(request, response);
        }

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService
                    .userDetailsService()
                    .loadUserByUsername(username);

            // Если токен валиден, то аутентифицируем пользователя
            try {
                if (jwtService.isTokenValid(tokenFromCookie, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);

                    updateToken(request, response);
                }
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
            }

        } else {
            try {
                if (jwtService.isTokenValid(tokenFromCookie, userService.getCurrentUser())) {
                    updateToken(request, response);
                }
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
            }
        }
        chain.doFilter(request, response);
    }

    private void updateToken(HttpServletRequest request, HttpServletResponse response) {
        String newToken = jwtService.generateToken(userService.getCurrentUser());
        cookieService.setCookieAuthorizationMaxAgeZero(request, response);
        cookieService.addTokenToCookie(response, newToken);
    }
}
