package br.com.jonyfs.config;

import br.com.jonyfs.security.MySavedRequestAwareAuthenticationSuccessHandler;
import br.com.jonyfs.security.RestAuthenticationEntryPoint;
import br.com.jonyfs.user.AppUserDetailsService;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@ComponentScan("br.com.jonyfs")
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @PostConstruct
    public void logMessage() {
        LOGGER.info("STARTED");
    }

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private MySavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
            .authorizeRequests()
            .antMatchers("/newUser", "/swagger-ui.html", "/swagger-resources", "/v2/api-docs", "/webjars/**")
            .permitAll()
            .antMatchers("/roles", "/privileges", "/users", "/me")
            .authenticated()
            .and()
            .formLogin()
            .successHandler(authenticationSuccessHandler)
            .failureHandler(new SimpleUrlAuthenticationFailureHandler())
            .and()
            .logout();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler() {
        return new MySavedRequestAwareAuthenticationSuccessHandler();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler myFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }
}
