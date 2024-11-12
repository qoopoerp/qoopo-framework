package net.qoopo.framework.security.jwt;

import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import lombok.Builder;
import net.qoopo.framework.security.authentication.user.UserData;
import net.qoopo.framework.security.core.token.Token;
import net.qoopo.framework.security.core.token.TokenProvider;

@Builder
public class JwtProvider implements TokenProvider {
    public static final SecretKey KEY = Jwts.SIG.HS256.key().build();

    @Builder.Default
    private String issuer = "https://www.qoopo.net";

    @Builder.Default
    private int timeOfLifeInMinutes = 60;

    private static String cleanToken(String token) {
        // String token = authorizationHeader.substring("Bearer".length()).trim();
        if (token.contains("Bearer ")) {
            token = token.replace("Bearer ", "");
        }
        token = token.trim();
        return token;
    }

    @Override
    public Token generate(UserData userdata) {
        // Calculamos la fecha de expiración del token
        Date issueDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issueDate);
        calendar.add(Calendar.MINUTE, timeOfLifeInMinutes);
        Date expireDate = calendar.getTime();

        // Creamos el token
        String jwtToken = Jwts.builder()
                .claim("rol", userdata.getPermissions())
                .subject(userdata.getUser())
                .issuer(issuer)
                .issuedAt(issueDate)
                .expiration(expireDate)
                // .signWith(SignatureAlgorithm.RS256, KEY)
                .signWith(KEY)
                // .signWith(SignatureAlgorithm.HS512, KEY)
                .compact();
        return Jwt.of(jwtToken);
    }

    @Override
    public boolean validate(Token token) {
        try {
            String jwt = cleanToken(token.getToken());
            // Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
            Jwts.parser().verifyWith(KEY).build()
                    .parseSignedClaims(jwt)
                    .getPayload()
                    .getIssuer()
                    .equals(issuer);
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
    }

    @Override
    public Token getToken(String token) {
        return Jwt.of(token);
    }
}
