package account.security;

import account.group.Group;
import account.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/signup/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/changepass/**")
                .hasAnyRole(Group.USER, Group.ACCOUNTANT, Group.ADMINISTRATOR)
                .antMatchers(HttpMethod.GET, "/api/empl/payment/**").hasAnyRole(Group.USER, Group.ACCOUNTANT)
                .antMatchers(HttpMethod.POST, "/api/acct/payments/**").hasRole(Group.ACCOUNTANT)
                .antMatchers(HttpMethod.PUT, "/api/acct/payments/**").hasRole(Group.ACCOUNTANT)
                .antMatchers(HttpMethod.GET, "/api/admin/user/**").hasRole(Group.ADMINISTRATOR)
                .antMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasRole(Group.ADMINISTRATOR)
                .antMatchers(HttpMethod.PUT, "/api/admin/user/role/**").hasRole(Group.ADMINISTRATOR)
                .antMatchers(HttpMethod.PUT, "/api/admin/user/access/**").hasRole(Group.ADMINISTRATOR)
                .antMatchers(HttpMethod.GET, "/api/security/events/**").hasRole(Group.AUDITOR)
                .antMatchers("/actuator/shutdown").permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        accessDeniedHandler.handle(request, response, accessDeniedException))
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}

