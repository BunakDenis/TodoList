package global.goit.edu.todolist.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
public class MyAppConfiguration {
/*

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
*/

}
