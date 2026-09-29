package com.dev.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    UUID id;
    String name;
    String image;
    Integer gender;
    String description;

    String categoryName;
    String storeName;

    BigDecimal minPrice;      // gia thap nhat trong cac bien the -> hien thi "tu 199.000d"
    Integer totalStock;       // tong ton kho cua cac bien the

    List<VariantResponse> variants;

    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    String createdBy;
}
