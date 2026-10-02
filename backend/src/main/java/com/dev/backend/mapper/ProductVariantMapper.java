package com.dev.backend.mapper;

import com.dev.backend.dto.request.ProductVariantRequest;
import com.dev.backend.dto.response.ProductVariantResponse;
import com.dev.backend.entities.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    //Mapping từ ProductVariant -> ProductVariantResponse
    /* ProductVariantResponse cần đánh @mapping cho những trường thông tin ở ProductVariant
    có hoặc khác tên so với ProductVariantResponse
     */
    @Mapping(source = "color.colorName", target ="colorName" )
    @Mapping(source = "size.sizeName", target = "sizeName")
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);

    @Mapping(source = "colorId", target = "color.id")
    @Mapping(source = "sizeId", target = "size.id")
    //@Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "product", ignore = true)
    ProductVariant toProductVariant(ProductVariantRequest productVariantRequest);

}
