package kutuphane.kutuphaneprojesi.Jwt;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        System.out.println(">>> Authorization header: [" + authHeader + "]");   // EKLE

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(">>> Header yok veya Bearer ile baslamiyor, filter atlaniyor");  // EKLE
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        System.out.println(">>> Cozulen token: [" + jwt + "]");   // EKLE

        final String username = jwtService.extractUsername(jwt);
        System.out.println(">>> Extracted username: [" + username + "]");   // EKLE

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean valid = jwtService.isTokenValid(jwt, userDetails);
            System.out.println(">>> Token gecerli mi: " + valid);   // EKLE

            if (valid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println(">>> SecurityContext'e yazildi!");   // EKLE
            }else {
                throw new RuntimeException(" token geçersiz");
            }
        }

        filterChain.doFilter(request, response);
}
}