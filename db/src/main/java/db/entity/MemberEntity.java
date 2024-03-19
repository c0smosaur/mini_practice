package db.entity;

import db.enums.MemberStatus;
import db.enums.MemberType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name="member")
public class MemberEntity extends BaseEntity{

    // email
    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    @Enumerated(value = EnumType.STRING)
    private MemberStatus status;

    @Column(nullable = false, length = 45)
    @Enumerated(value = EnumType.STRING)
    private MemberType type;

}
