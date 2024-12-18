package global.goit.edu.todolist.model.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieService {

    @Value("${cookie.token.header.name}")
    private String headerName;

    @Value("${cookie.token.expiration.time}")
    private int expirationTime;

    public String getToken(HttpServletRequest request) {
        String result = "";
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(headerName)) {
                    result = cookie.getValue();
                }
            }
        }
        return result;
    }

    public void addToken(HttpServletResponse response, String token) {

        Cookie cookie = new Cookie(headerName, token);

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(expirationTime);

        response.addCookie(cookie);
    }

    public void setExpirationTimeByZeroToToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(headerName)) {
                    cookie.setMaxAge(0);
                    cookie.setHttpOnly(false);

                    response.addCookie(cookie);
                }
            }
        }
    }

    public void updateToken(HttpServletRequest request, HttpServletResponse response, String token) {
        setExpirationTimeByZeroToToken(request, response);
        addToken(response, token);
    }
}