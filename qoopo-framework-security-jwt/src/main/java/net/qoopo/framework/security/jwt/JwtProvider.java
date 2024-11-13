package net.qoopo.framework.security.jwt;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Builder;
import net.qoopo.framework.security.authentication.user.UserData;
import net.qoopo.framework.security.core.permission.DefaultGrantedPermission;
import net.qoopo.framework.security.core.token.Token;
import net.qoopo.framework.security.core.token.TokenProvider;

@Builder
public class JwtProvider implements TokenProvider {

    private static Logger log = Logger.getLogger("JwtProvider");
    private static SecretKey SECURE_RANDOM_KEY = Jwts.SIG.HS256.key().build();

    private SecretKey KEY;

    /**
     * Para generar una clave que cumpla con la longitud minima en base64 se puede
     * usar en bash:
     * openssl rand -base64 32
     */
    private String secretKeyB64;

    @Builder.Default
    private String issuer = "https://www.qoopo.net";

    @Builder.Default
    private int timeOfLifeInMinutes = 30;

    private static String cleanToken(String token) {
        // String token = authorizationHeader.substring("Bearer".length()).trim();
        if (token.contains("Bearer ")) {
            token = token.replace("Bearer ", "");
        }
        token = token.trim();
        return token;
    }

    private SecretKey getKey() {
        if (KEY == null) {
            if (secretKeyB64 != null) {
                try {
                    KEY = new SecretKeySpec(Base64.getDecoder().decode(secretKeyB64), "HmacSHA256");
                } catch (Exception e) {
                    log.warning(
                            "No se especificó una clave que cumpla con el mínimo necesario de 32 bits. Se genera una clave aleatoria");
                    KEY = SECURE_RANDOM_KEY;
                }
            } else
                KEY = SECURE_RANDOM_KEY;
        }
        return KEY;
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
                .claim("permissions",
                        userdata.getPermissions().stream().map(c -> c.getPermission()).collect(Collectors.joining(",")))
                .subject(userdata.getUser())
                .issuer(issuer)
                .issuedAt(issueDate)
                .expiration(expireDate)
                .signWith(getKey())
                // .signWith(SignatureAlgorithm.HS256, secretKey)

                // .signWith(SignatureAlgorithm.HS512, KEY)
                .compact();
        return Jwt.of(jwtToken, userdata.getUser(), userdata.getPermissions());
    }

    @Override
    public boolean validate(String token) {
        try {
            token = cleanToken(token);
            // Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
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
        token = cleanToken(token);
        // Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Jwt.of(token, claims.getSubject(),
                DefaultGrantedPermission.of(claims.get("permissions").toString().split(",")));
    }
}
