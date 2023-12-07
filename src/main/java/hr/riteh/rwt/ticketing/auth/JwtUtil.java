package hr.riteh.rwt.ticketing.auth;

import hr.riteh.rwt.ticketing.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "x8D6F05lyvVZaJmljXognjrsjYI9r9LtsA9SdIzVwd95o5aGyRDX6seHm6y77q5b1WEQzTVQF9DT7Q2G1eykeYFsEC5RCiVUGe4dglTaIMgXZ6fZpNsYZB4iL8eoz7h4gec1738SaMuxLl49qPqUe41dmGsLxGWmta5pvByIYxhewEuLmYCLuWzczXNz1ORwnV0kKb0b7M7ZY9C0qcmoQ9VAkUzQeXBXSEh1ndFgTRqK2r7KTmTtEFhz9XiDOXXS";
    private final int TOKEN_VALIDITY = 60 * 4;  //4 sata

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";


    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUserID());
        claims.put("firstName",user.getFirstName());
        claims.put("lastName",user.getLastName());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(this.TOKEN_VALIDITY));
        return TOKEN_PREFIX + Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken == null) {
            throw new RuntimeException("No authorization header");
        }
        else if (bearerToken.isBlank()) {
            throw new RuntimeException("No token in authorization header");
        }
        else if (bearerToken.equals(TOKEN_PREFIX.substring(0, TOKEN_PREFIX.length()-1))){
            throw new RuntimeException("No token found");
        }

        else if (bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }

        throw new RuntimeException("Wrong token type or format");
    }

    public Claims resolveClaims(String token) {
        if (token != null) {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }

}
