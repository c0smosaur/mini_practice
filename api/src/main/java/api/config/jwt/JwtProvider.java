package api.config.jwt;

import java.util.Map;

public interface JwtProvider {

    TokenDto generateToken(Map<String, Object> data);
    Map<String, Object> validateTokenAndThrow(String token);
}
