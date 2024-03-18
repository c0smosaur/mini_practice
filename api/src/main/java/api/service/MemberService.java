package api.service;

import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.config.jwt.JwtTokenProvider;
import api.config.jwt.TokenDto;
import api.config.jwt.TokenResponse;
import api.converter.MemberConverter;
import api.model.MemberLoginRequest;
import api.model.MemberRegisterRequest;
import api.model.MemberResponse;
import db.entity.MemberEntity;
import db.enums.MemberStatus;
import db.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberConverter memberConverter;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberResponse register(MemberRegisterRequest request) {
        MemberEntity entity = memberConverter.toEntity(request);
        MemberEntity newEntity = registerMember(entity);
        MemberResponse response = memberConverter.toResponse(newEntity);

        return response;
    }

    public MemberEntity registerMember(MemberEntity memberEntity){
        return Optional.ofNullable(memberEntity)
                .map(it -> {
                    it.setPassword(passwordEncoder.encode(it.getPassword()));
                    it.setStatus(MemberStatus.REGISTERED);

                    return memberRepository.save(it);
                })
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));
    }

    public TokenResponse signIn(MemberLoginRequest request) {

    }

    public TokenResponse memberSignIn(MemberLoginRequest request){
        MemberEntity entity = memberRepository.findFirstByUsernameAndStatus(
                request.getUsername(), MemberStatus.REGISTERED)
                .filter(it -> passwordEncoder.matches(request.getPassword(), it.getPassword()))
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));

        Map<String, Object> data = new HashMap<>();
        data.put("username",request.getUsername());
        data.put("password", request.getPassword());

        TokenDto accessToken = jwtTokenProvider.generateAccessToken(data);
        TokenDto refreshToken = jwtTokenProvider.generateRefreshToken(data);

        // TODO validate token and return tokenresponse
        // TODO create filter to authenticate jwt
//        return
    }

}
