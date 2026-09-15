package hei.school.prog3rattrapage.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY = "test";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String key = request.getHeader("x-api-key");

        if (!API_KEY.equals(key)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                    {
                      "code":"UNAUTHORIZED",
                      "message":"Invalid API key"
                    }
                    """);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
