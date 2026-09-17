package com.dev.backend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

/** 1 bien the khi tao san pham: chon mau + size co san, nhap gia + ton kho */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantRequest {
    UUID colorId;
    UUID sizeId;
    String sku;
    BigDecimal price;
    Integer stock;
}
