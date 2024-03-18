package db.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProductStatus {

    CART("장바구니"),
    HISTORY("계약종료"),
    ;

    private String description;
}
