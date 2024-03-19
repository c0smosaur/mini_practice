package api.config.jwt;

import api.common.error.TokenErrorCode;
import api.common.exception.ResultException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider implements JwtProvider {

    private String SECRET_KEY = "MiniProjectPracticeJWTSecretKey1234567890";

//    TODO why do i keep getting a null for this when the other two have no problems being injected
//    @Value("${jwt.secretKey}")
//    private String SECRET_KEY;

    @Value("${jwt.accessToken.plus-hour}")
    private Long ACCESS_TOKEN_PLUS_HOUR;
    @Value("${jwt.refreshToken.plus-hour}")
    private Long REFRESH_TOKEN_PLUS_HOUR;

    String keyBase64Encoded = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    SecretKey key = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());

    public Date generateTime(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(
                ZoneId.systemDefault()
        ).toInstant());
    }

    @Override
    public TokenDto generateToken(Map<String, Object> data){

        LocalDateTime accessExpiredAt = LocalDateTime.now().plusHours(ACCESS_TOKEN_PLUS_HOUR);
        LocalDateTime refreshExpiredAt = LocalDateTime.now().plusHours(REFRESH_TOKEN_PLUS_HOUR);

        String accessToken = Jwts.builder()
                .signWith(key)
                .setClaims(data)
                .setIssuer("test")
                .setIssuedAt(generateTime(LocalDateTime.now()))
                .setExpiration(generateTime(accessExpiredAt))
                .compact();

        String refreshToken = Jwts.builder()
                .signWith(key)
                .setClaims(data)
                .setIssuer("test")
                .setIssuedAt(generateTime(LocalDateTime.now()))
                .setExpiration(generateTime(refreshExpiredAt))
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .accessExpiredAt(accessExpiredAt)
                .refreshToken(refreshToken)
                .refreshExpiredAt(refreshExpiredAt)
                .build();
    }

    @Override
    public Map<String, Object> validateTokenAndThrow(String token){
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        try {
            var result = parser.parseClaimsJws(token);
            return new HashMap<String, Object>(result.getBody());
        } catch (Exception e){
            if (e instanceof SignatureException){
                throw new ResultException(TokenErrorCode.INVALID_TOKEN);
            } else if (e instanceof ExpiredJwtException){
                throw new ResultException(TokenErrorCode.EXPIRED_TOKEN);
            } else {
                throw new ResultException(TokenErrorCode.TOKEN_EXCEPTION);
            }
        }
    }
}
