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
public class ProductUpdateRequest {
    String name;
    String image;
    Integer gender;
    String description;
    UUID categoryId;
    UUID storeId;
    List<ProductVariantUpdateRequest> variantUpdateRequests;
}
