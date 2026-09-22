package com.dev.backend.mapper;

import com.dev.backend.dto.response.SizeResponse;
import com.dev.backend.entities.Size;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    @Mapping(source = "sortOrder", target = "sortOrderSize")
    SizeResponse sizeResponse(Size size);
}
