package com.dev.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantUpdateResponse {
    String colorName;
    String sizeName;
    String sku;
    BigDecimal price;
    int stock;
    LocalDateTime updatedDate;
}
