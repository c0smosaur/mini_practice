package api.service;

import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.config.jwt.JwtTokenProvider;
import api.config.jwt.TokenDto;
import api.converter.MemberConverter;
import api.model.MemberLoginRequest;
import api.model.MemberLoginResponse;
import api.model.MemberRegisterRequest;
import api.model.MemberRegisterResponse;
import db.entity.MemberEntity;
import db.enums.MemberStatus;
import db.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberConverter memberConverter;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberRegisterResponse register(MemberRegisterRequest request) {
        MemberEntity entity = memberConverter.toEntity(request);
        MemberEntity newEntity = registerMember(entity);
        MemberRegisterResponse response = memberConverter.toResponse(newEntity);

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

    public MemberLoginResponse memberSignIn(MemberLoginRequest request){
        MemberEntity entity = memberRepository.findFirstByUsernameAndStatus(
                        request.getUsername(),
                        MemberStatus.REGISTERED)
                .filter(it -> passwordEncoder.matches(request.getPassword(), it.getPassword()))
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));

        Map<String, Object> data = new HashMap<>();
        data.put("username",entity.getUsername());
        data.put("password", entity.getPassword());

        TokenDto token = jwtTokenProvider.generateToken(data);

        jwtTokenProvider.validateTokenAndThrow(token.getAccessToken());

        return MemberLoginResponse.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .status(entity.getStatus())
                .type(entity.getType())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

}
