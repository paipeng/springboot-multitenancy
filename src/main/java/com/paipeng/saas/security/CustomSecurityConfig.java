package com.paipeng.saas.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {
    private final static Logger logger = LogManager.getLogger(CustomSecurityConfig.class.getSimpleName());

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * This is where access to various resources (urls) in the application is
     * defined
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("configure");
        //@formatter:off
        http.csrf().disable().cors().and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/css/**", "/index").permitAll()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/users/login").permitAll()
                .antMatchers("/version").authenticated()
                .and()
                .formLogin().loginPage("/login")
                .and()
                .logout()
                .logoutUrl("/logout");
        //@formatter:on
    }

    /**
     * Create an instance of the custom authentication filter which intercepts
     * and processes the end user's login form submission for further
     * authentication processing. This filter is added before other filters so
     * that it can intercept the user login form submission and extract the the
     * additional 'tenant' field
     *
     * @return
     * @throws Exception
     */
    @Bean
    public JWTAuthorizationFilter authenticationFilter() throws Exception {
        logger.info("authenticationFilter");
        //CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        JWTAuthorizationFilter filter = new JWTAuthorizationFilter();
        //filter.setAuthenticationManager(authenticationManagerBean());
        //filter.setAuthenticationFailureHandler(failureHandler());
        //filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(authProvider());
    }

    /**
     * Authentication provider which provides the logged in user's credentials
     * for verification and authentication if they are coeect
     *
     * @return
     */
    public AuthenticationProvider authProvider() {
        logger.info("authProvider");
        // The custom authentication provider defined for this app
        CustomUserDetailsAuthenticationProvider provider = new CustomUserDetailsAuthenticationProvider(passwordEncoder(), userDetailsService);
        return provider;
    }

    /**
     * The page to show if authentication fails
     *
     * @return
     */
    public SimpleUrlAuthenticationFailureHandler failureHandler() {
        logger.info("failureHandler");
        return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
    }

    public SimpleUrlAuthenticationSuccessHandler successHandler() {
        logger.info("successHandler");
        return new SimpleUrlAuthenticationSuccessHandler("/user/index");
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
