package global.goit.edu.todolist.config;

import global.goit.edu.todolist.controller.filter.JwtCreateTokenFilter;
import global.goit.edu.todolist.model.service.JwtService;
import global.goit.edu.todolist.model.service.UserService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "global.goit.edu.todolist.controller.repository")
@ComponentScan(basePackages = "global.goit.edu.todolist")
public class MyAppConfiguration {

    private final JwtService jwtService;

    private final UserService userService;

    public MyAppConfiguration(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Bean
    public FilterRegistrationBean<JwtCreateTokenFilter> createTokenFilter(){
        FilterRegistrationBean<JwtCreateTokenFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JwtCreateTokenFilter(jwtService, userService));
        registrationBean.addUrlPatterns("/**");
        registrationBean.setOrder(1);

        return registrationBean;
    }

}
