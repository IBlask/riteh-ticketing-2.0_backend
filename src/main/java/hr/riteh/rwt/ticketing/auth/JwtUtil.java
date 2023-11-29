package hr.riteh.rwt.ticketing.auth;

import hr.riteh.rwt.ticketing.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {


    private final String secret_key = "x8D6F05lyvVZaJmljXognjrsjYI9r9LtsA9SdIzVwd95o5aGyRDX6seHm6y77q5b1WEQzTVQF9DT7Q2G1eykeYFsEC5RCiVUGe4dglTaIMgXZ6fZpNsYZB4iL8eoz7h4gec1738SaMuxLl49qPqUe41dmGsLxGWmta5pvByIYxhewEuLmYCLuWzczXNz1ORwnV0kKb0b7M7ZY9C0qcmoQ9VAkUzQeXBXSEh1ndFgTRqK2r7KTmTtEFhz9XiDOXXS";
    private final int tokenValidity = 60*60*1000;


    JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";


    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUserID());
        claims.put("firstName",user.getFirstName());
        claims.put("lastName",user.getLastName());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(this.tokenValidity));
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

}
