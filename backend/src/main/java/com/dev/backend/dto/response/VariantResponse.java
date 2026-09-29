package com.dev.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

/** 1 bien the cua san pham: mau + size + gia + ton kho */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantResponse {
    UUID id;
    String sku;
    String colorName;
    String hexCode;
    String sizeName;
    BigDecimal price;
    Integer stock;
}
