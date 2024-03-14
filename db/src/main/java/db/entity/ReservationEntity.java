package db.entity;

import db.enums.ReservationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name="reservation")
public class ReservationEntity extends BaseEntity{

    private Long memberId;
    private Long productId;
    private ReservationStatus status;

}
