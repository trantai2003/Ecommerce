package com.dev.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRequest {
    @NotBlank(message = "Tên cửa hàng không được để trống")
    String storeImage;
    String storeName;
    String storeDescriptions;
    UUID ownerId;
}
