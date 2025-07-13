package su.taskmanager.controller.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;


@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Long userId, String username) {


        Date now = new Date();
        // Это длина валидности токена, не считал сколько это в секундах, но предполагается много
        long VALIDITY_IN_MILLISECONDS = 360000000000L;
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_IN_MILLISECONDS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;

        }   catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build();

        Claims claims = parser.parseClaimsJws(token).getBody();

        return Long.parseLong(claims.getSubject());
    }



    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
