package com.dev.backend.dto.response;

import com.dev.backend.entities.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {
    String storeName;
    String description;
    String storeImage;
    String ownerName;
    User user;
}
