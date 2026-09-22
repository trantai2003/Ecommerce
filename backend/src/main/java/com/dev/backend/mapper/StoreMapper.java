package com.dev.backend.mapper;

import com.dev.backend.dto.response.StoreResponse;
import com.dev.backend.entities.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    @Mapping(source = "owner.name", target = "ownerName")   // store.getOwner().getName() -> ownerName
    @Mapping(source = "owner", target = "user")
    StoreResponse storeResponse(Store store);
}
