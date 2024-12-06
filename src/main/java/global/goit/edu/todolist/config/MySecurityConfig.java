package global.goit.edu.todolist.config;


import global.goit.edu.todolist.controller.filter.CookieFilter;
import global.goit.edu.todolist.controller.filter.JwtAuthenticationFilter;
import global.goit.edu.todolist.controller.filter.JwtCreateTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class MySecurityConfig {

    @Autowired
    private final DataSource dataSource;
    private final CookieFilter cookieFilter;
    private final JwtCreateTokenFilter jwtCreateTokenFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public MySecurityConfig(DataSource dataSource, CookieFilter cookieFilter,
                            JwtCreateTokenFilter jwtCreateTokenFilter,
                            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.dataSource = dataSource;
        this.cookieFilter = cookieFilter;
        this.jwtCreateTokenFilter = jwtCreateTokenFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select username, role from users where username=?");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //.addFilterAfter(jwtCreateTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .logout(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }

}
