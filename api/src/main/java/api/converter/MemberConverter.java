package api.converter;

import api.common.error.GeneralErrorCode;
import api.common.exception.ResultException;
import api.model.MemberRegisterRequest;
import api.model.MemberRegisterResponse;
import db.entity.MemberEntity;
import db.enums.MemberStatus;
import db.enums.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class MemberConverter {

    public MemberEntity toEntity(MemberRegisterRequest request){
        return Optional.ofNullable(request)
                .map(it -> {
                    return MemberEntity.builder()
                            .username(it.getUsername())
                            .password(it.getPassword())
                            .name(it.getName())
                            .status(MemberStatus.REGISTERED)
                            .type(MemberType.USER)
                            .build();
                })
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));
    }

    public MemberRegisterResponse toResponse(MemberEntity entity) {
        return Optional.ofNullable(entity)
                .map(it -> {
                    return MemberRegisterResponse.builder()
                            .id(it.getId())
                            .username(it.getUsername())
                            .status(it.getStatus())
                            .type(it.getType())
                            .build();
                })
                .orElseThrow(() -> new ResultException(GeneralErrorCode.NULL_POINT));
    }
}
