package global.goit.edu.todolist.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@Order(2)
@WebFilter("/**")
public class CookieFilter implements Filter {

    private static final String SESSION_ID_COOKIE_NAME = "SESSIONID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (checkCookie(httpServletRequest, httpServletResponse)) {
            // Если cookie JSESSIONID существует, имитируем аутентификацию
            // Пример: создаем пользователя с ролями
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    new User("authenticatedUser", "", new ArrayList<>()), null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            httpServletResponse.sendRedirect(httpServletRequest.getRequestURI());
        } else {
            httpServletResponse.sendRedirect("/login");
        }
    }

    private boolean checkCookie(HttpServletRequest request, HttpServletResponse response) {

        boolean existCookie = Arrays.stream(request.getCookies())
                .anyMatch(cookie -> cookie.getName().equalsIgnoreCase(SESSION_ID_COOKIE_NAME));
        System.out.println("existCookie = " + existCookie);
        if (!existCookie) {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            response.addCookie(new Cookie(SESSION_ID_COOKIE_NAME, details.getSessionId()));
        }
        return existCookie;
    }

}
