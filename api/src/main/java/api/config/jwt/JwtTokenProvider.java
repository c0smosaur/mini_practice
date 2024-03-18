package api.config.jwt;

import api.common.error.TokenErrorCode;
import api.common.exception.ResultException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider{

    @Value("${token.secret.key}")
    private String SECRET_KEY;
    @Value("${token.accessToken.plus-hour}")
    private Long ACCESS_TOKEN_PLUS_HOUR;
    @Value("${token.refreshToken.plus-hour}")
    private Long REFRESH_TOKEN_PLUS_HOUR;

    SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public Date generateTime(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(
                ZoneId.systemDefault()
        ).toInstant());
    }

    public TokenDto generateAccessToken(Map<String, Object> data){

        LocalDateTime expiredAt = LocalDateTime.now().plusHours(ACCESS_TOKEN_PLUS_HOUR);

        String accessToken = Jwts.builder()
                .signWith(key)
                .setClaims(data)
                .setIssuer("test")
                .setIssuedAt(generateTime(LocalDateTime.now()))
                .setExpiration(generateTime(expiredAt))
                .compact();

        return TokenDto.builder()
                .token(accessToken)
                .expiredAt(expiredAt)
                .build();
    }

    public TokenDto generateRefreshToken(Map<String, Object> data){

        LocalDateTime expiredAt = LocalDateTime.now().plusHours(REFRESH_TOKEN_PLUS_HOUR);

        String refreshToken = Jwts.builder()
                .signWith(key)
                .setClaims(data)
                .setIssuer("test")
                .setIssuedAt(generateTime(LocalDateTime.now()))
                .setExpiration(generateTime(expiredAt))
                .compact();

        return TokenDto.builder()
                .token(refreshToken)
                .expiredAt(expiredAt)
                .build();
    }

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
