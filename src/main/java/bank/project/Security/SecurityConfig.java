package bank.project.Security;
import bank.project.Jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // REST API için CSRF kapatılır
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT stateless'tır, session tutulmaz
                .authorizeHttpRequests(auth -> auth

                        /* .requestMatchers("/login").permitAll()
                         .requestMatchers("/register").permitAll()

                         .requestMatchers("/admin/get/users").hasRole("ADMIN")
                         .requestMatchers("/add/book").hasRole("ADMIN")
                         .requestMatchers("/delete/book").hasRole("ADMIN")
                         .requestMatchers("/admin/get/books").hasRole("ADMIN")
                         .requestMatchers("/get/barrowedbooks").hasRole("ADMIN")
                         .requestMatchers("/get/allreadinghistory").hasRole("ADMIN")

                         .requestMatchers("/user/get/books").hasRole("USER")
                         .requestMatchers("/user/barrow/book").hasRole("USER")
                         .requestMatchers("/get/mybooks").hasRole("USER")
                         .requestMatchers("/get/readinghistory").hasRole("USER")
                         .requestMatchers("/user/return/book").hasRole("USER")
                         .anyRequest().authenticated()  */


                        .anyRequest().permitAll()// Diğer her yer token ister
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"" + authException.getMessage() + "\"}");
                        })
                )

                // İŞTE BURADA BAĞLIYORUZ:
                // Standart kullanıcı adı/şifre kontrolünden ÖNCE bizim JWT filtremiz çalışsın diyoruz.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT filtren

        return http.build();
    }
}
