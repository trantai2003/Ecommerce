package com.dev.backend.mapper;

import com.dev.backend.dto.request.ColorRequest;
import com.dev.backend.dto.response.ColorResponse;
import com.dev.backend.entities.Color;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ColorMapper {

    // Request -> Entity: id do DB sinh, khong lay tu client
    //Request thiếu ID so vs Entity nên cần mapping mỗi ID
    @Mapping(target = "id", ignore = true)
    Color toColor(ColorRequest request);

    // Entity -> Response
    ColorResponse toResponse(Color color);

    List<ColorResponse> toResponseList(List<Color> colors);
}
