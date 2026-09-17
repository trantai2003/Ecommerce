package com.dev.backend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCreateRequest {
    String name;
    String image;
    Integer gender;
    String description;
    UUID categoryId;
    UUID storeId;
    // Gia + ton kho nam trong tung bien the (mau + size)
    List<ProductVariantRequest> variants;
}
