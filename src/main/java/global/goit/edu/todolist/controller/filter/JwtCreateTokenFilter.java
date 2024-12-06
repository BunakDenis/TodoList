package global.goit.edu.todolist.controller.filter;

import global.goit.edu.todolist.model.entity.user.User;
import global.goit.edu.todolist.model.service.JwtService;
import global.goit.edu.todolist.model.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static global.goit.edu.todolist.controller.filter.JwtAuthenticationFilter.BEARER_PREFIX;

@Data
@Component
@RequiredArgsConstructor
@Order(1)
public class JwtCreateTokenFilter implements Filter {


    private final JwtService jwtService;

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        User currentUser = userService.getCurrentUser();

        // Получаем токен из заголовка
        var authHeader = httpServletRequest.getHeader(JwtAuthenticationFilter.HEADER_NAME);
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {


            var jwt = jwtService.generateToken(currentUser);
            System.out.println("jwt = " + jwt);
            httpServletResponse.addHeader(JwtAuthenticationFilter.HEADER_NAME, jwt);

            chain.doFilter(request, response);
        }

    }
}
