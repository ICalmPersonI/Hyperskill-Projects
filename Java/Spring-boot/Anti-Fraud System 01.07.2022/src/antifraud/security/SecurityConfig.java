package antifraud.security;

import antifraud.user.UserDetailsServiceImpl;
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

    UserDetailsServiceImpl service;

    RestAuthenticationEntryPoint authenticationEntryPoint;

    SecurityConfig(UserDetailsServiceImpl service, RestAuthenticationEntryPoint entryPoint) {
        this.service = service;
        this.authenticationEntryPoint = entryPoint;
    }

    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(service);
        provider.setPasswordEncoder(encoder());
        return provider;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(encoder().encode("password"))
                .roles("ADMINISTRATOR");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/auth/user/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/auth/access/**").hasRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.PUT, "/api/auth/role/**").hasRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.GET, "/api/auth/list/**").hasAnyRole("ADMINISTRATOR", "SUPPORT")
                .antMatchers(HttpMethod.DELETE, "/api/auth/user/**").hasRole("ADMINISTRATOR")

                .antMatchers(HttpMethod.POST, "/api/antifraud/transaction/**").hasRole("MERCHANT")
                .antMatchers(HttpMethod.POST, "/api/antifraud/access/**").hasRole("ADMINISTRATOR")

                .antMatchers("/api/antifraud/suspicious-ip/**").hasRole("SUPPORT")

                .antMatchers("/api/antifraud/stolencard/**").hasRole("SUPPORT")

                .antMatchers(HttpMethod.GET, "/api/antifraud/history/**").hasRole("SUPPORT")

                .antMatchers(HttpMethod.PUT, "/api/antifraud/transaction/**").hasRole("SUPPORT")
                .antMatchers("/actuator/shutdown").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
