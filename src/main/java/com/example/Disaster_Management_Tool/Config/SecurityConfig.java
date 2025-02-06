package com.example.Disaster_Management_Tool.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(customizer -> customizer.disable()).
                authorizeHttpRequests(request -> request
                        .requestMatchers("/userlogin/register", "/userlogin/login" , "/userlogin/logout" ,"/sos-alerts","/userlogin/check").permitAll()
                        .requestMatchers("/userlogin/sendOtp","/userlogin/verifyOtp").permitAll()
                        .requestMatchers("/disaster-report/submit").permitAll()
                        .requestMatchers("/otp/send", "otp/verify").permitAll()
                        .requestMatchers("/disaster-report/user-reports","/disaster-report/admin-reports").permitAll()
                        .requestMatchers("/disaster-report/review/{id}").permitAll()
                        .requestMatchers("/disaster-report/completed/{id}").permitAll()
                        .requestMatchers("/disaster-report/assign-team").permitAll()
                        .requestMatchers("/team_assign/addteam").permitAll()
                        .requestMatchers("/team_assign/getAllTeams").permitAll()
                        .requestMatchers("/review-report/addReviewReport").permitAll()
                        .requestMatchers("/team_assign/unassign/{teamId}").permitAll()
                        .requestMatchers("/disaster-report/search-report/{reportId}").permitAll()
                        .requestMatchers("/disaster-report/fetchAllReportOfUsers/{userId}").permitAll()
                        .requestMatchers("/disaster-report/reject/{id}").permitAll()

                        .anyRequest().authenticated()).
                httpBasic(Customizer.withDefaults()).
                sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("kiran")
//                .password("k@123")
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("harsh")
//                .password("h@123")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);


        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }

}


