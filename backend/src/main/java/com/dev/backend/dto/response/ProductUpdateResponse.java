package com.dev.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateResponse {
    String name;
    String image;
    Integer gender;
    String description;
    String categoryName;
    String storeName;
    LocalDateTime updatedDate;

    List<ProductVariantUpdateResponse> variantUpdateResponses;


}
