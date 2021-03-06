package com.webchat.config;

import com.webchat.config.props.AppProperties;
import com.webchat.security.jwt.JwtConfigurer;
import com.webchat.security.jwt.JwtTokenProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String AUTH_ENDPOINTS = "/auth/**";
    private static final String WEBSOCKET_ENDPOINTS = "/ws/**";

    private final JwtTokenProviderImpl jwtTokenProvider;
    private final AppProperties appProperties;

    @Autowired
    public SecurityConfig(JwtTokenProviderImpl jwtTokenProvider, AppProperties appProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.appProperties = appProperties;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new CORSResponseFilter(appProperties.getFrontendUrl()), SessionManagementFilter.class)
                .cors()
                .and()
                .csrf().disable()  // АККУРАТНЕЕ С ЭТОЙ ХУЕТОЙ!!!
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                .antMatchers(AUTH_ENDPOINTS, WEBSOCKET_ENDPOINTS, "/email/**").permitAll()

                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/room/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/room/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/room/**").hasRole("ADMIN")

                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}