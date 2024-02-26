package com.example.demo.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.OAuth2LoginAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception, BeanCreationException {
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeHttpRequests(Authorize -> Authorize
				.requestMatchers("/api/**").authenticated()
				.anyRequest().permitAll()
				)
		.addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
		.csrf().disable()
		.cors().configurationSource(corsConfigurationSource())
		.and()
		.oauth2Login()
		.and()
		.httpBasic().and()
		.formLogin();
		
		return http.build();
		
	}
	
    // CORS Configuration
    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
		    var cfg = new CorsConfiguration();
		    cfg.setAllowedOrigins(Arrays.asList(
		        "http://localhost:3000"
//                    "http://localhost:4000",
//                    "http://localhost:4200",
//                    "https://twitter-clone-two-woad.vercel.app",
//                    "https://twitter-clone-six-kohl.vercel.app"
		    ));
		    cfg.setAllowedMethods(Collections.singletonList("*"));
		    cfg.setAllowCredentials(true);
		    cfg.setAllowedHeaders(Collections.singletonList("*"));
		    cfg.setExposedHeaders(Arrays.asList("Authorization"));
		    cfg.setMaxAge(3600L);
		    return cfg;
		};
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
