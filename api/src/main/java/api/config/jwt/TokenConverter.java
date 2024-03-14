package api.config.jwt;

import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class TokenConverter {

    public TokenResponse toResponse(
            TokenDto accessToken, TokenDto refreshToken
    ) {
        Objects.requireNonNull(accessToken, () -> {
            throw new ResultException(GeneralErrorCode.NULL_POINT);
        });

        Objects.requireNonNull(refreshToken, () -> {
            throw new ResultException(GeneralErrorCode.NULL_POINT);
        });

        return TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .build();
    }
}
