package db.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReservationStatus {
    PENDING("예약 상태"),
    TERMINATED("숙박 종료"),
    ;

    private String description;
}
