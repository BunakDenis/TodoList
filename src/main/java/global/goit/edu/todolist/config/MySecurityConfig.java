package global.goit.edu.todolist.config;


import global.goit.edu.todolist.controller.filter.JwtAuthenticationFilter;
import global.goit.edu.todolist.model.entity.user.User;
import global.goit.edu.todolist.model.service.CookieService;
import global.goit.edu.todolist.model.service.JwtService;
import global.goit.edu.todolist.model.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class MySecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtService jwtService;
    private final UserService userService;
    private final CookieService cookieService;
    @Value("${cookie.token.header.name}")
    private String cookieHeaderName;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                            request.requestMatchers("/login").permitAll()
                                    .anyRequest().authenticated();
                        }

                )
                .formLogin(form -> {
                    form
                            .successHandler((request, response, authentication) -> {
                                // После успешной авторизации создаём токен и добавляем его в Cookie
                                User currentUser = userService.getCurrentUser();
                                String token = jwtService.generateToken(currentUser);
                                cookieService.addToken(response, token);
                                response.sendRedirect("/note/list");
                            }).permitAll();
                })
                .logout(logout -> {
                    logout.deleteCookies(cookieHeaderName)
                            .logoutSuccessUrl("/login");
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userService.userDetailsService())
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.getOrBuild();
    }
}