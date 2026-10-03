package com.dev.backend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantUpdateRequest {
    UUID variantId;
    UUID colorId;
    UUID sizeId;
    String sku;
    BigDecimal price;
    int stock;
}
