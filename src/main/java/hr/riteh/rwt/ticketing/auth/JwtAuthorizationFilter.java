package hr.riteh.rwt.ticketing.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.riteh.rwt.ticketing.dto.SuccessDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ObjectMapper mapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (uriIsProtected(request)) {
            try {
                String resolvedToken = jwtUtil.resolveToken(request);

                Claims claims = jwtUtil.resolveClaims(resolvedToken);

                if (claims != null && jwtUtil.validateClaims(claims)) {
                    String userID = claims.getSubject();
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userID, "", new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                SuccessDto successDto = new SuccessDto();
                successDto.setSuccessFalse(e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                mapper.writeValue(response.getWriter(), successDto);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private boolean uriIsProtected (@NonNull HttpServletRequest request) {
        ArrayList<String> publicURIs = new ArrayList<>();
        publicURIs.add("/api/h2-console/");
        publicURIs.add("/api/auth/login/");

        return publicURIs.stream().noneMatch(request.getRequestURI().concat("/")::contains);
    }

}
