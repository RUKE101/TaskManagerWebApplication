package su.taskmanager.controller.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Builder
@Component
public class JwtTokenProvider {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Long validityInMilliseconds = Long.parseLong("360000000000");
    public String createToken(Long userId, String username) {


        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseClaimsJws(token);
            return true;

        }   catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith((SecretKey) key)
                .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();

        return Long.parseLong(claims.getSubject());
    }

}
