package api.model;

import db.enums.MemberStatus;
import db.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginResponse {

    private Long id;
    private String username;
    private MemberStatus status;
    private MemberType type;
    private String accessToken;
    private String refreshToken;

}
