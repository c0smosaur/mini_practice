package api.service;

import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.config.jwt.TokenDto;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberConverter memberConverter;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

    public MemberResponse signIn(MemberLoginRequest request) {

    }

    public MemberEntity memberSignIn(MemberLoginRequest request){
        MemberEntity entity = memberRepository.findFirstByUsernameAndStatus(
                request.getUsername(), MemberStatus.REGISTERED)
                .filter(it -> passwordEncoder.matches(request.getPassword(), it.getPassword()))
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));

        String token =
    }

}
