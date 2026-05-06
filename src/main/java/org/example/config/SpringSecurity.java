package org.example.config;

import org.example.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity{

//    @Autowired
//    private AuthService authService; //Autowired your custom created auth service. Make sure it is implementing user Detail service inteface
        //Here note that no need to call auth service since spring security automatically picks the auth service since it implements user detail service
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // needed for POST APIs

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // allow login & register
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable()) // ❌ disable default login page
                .httpBasic(basic -> basic.disable()); // optional (disable basic auth)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();  //This bcrypt password encoder is the implementation class which implements password encoder
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();  //This is a configuration class
    }
    //Authentication manager is the component responsible for verifying credentials.
    /*
    The above code means
    Give me Spring Security's internally configured AuthenticationManager
and register it as a bean so I can inject and use it manually.
     */

}
