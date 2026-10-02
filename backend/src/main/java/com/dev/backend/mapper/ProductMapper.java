package com.dev.backend.mapper;

import com.dev.backend.dto.request.ProductCreateRequest;
import com.dev.backend.dto.response.ProductResponse;
import com.dev.backend.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    /*
    Chuyển từ ProductCreateRequest -> Product
    Cần chuyển từ request -> entity vì jpa chỉ nhận entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categoryId", target = "categoryProduct.id")
    @Mapping(source = "storeId", target = "store.id")
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    //target (source)
    Product toProduct(ProductCreateRequest productCreateRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categoryProduct.categoryName", target = "categoryName")
    @Mapping(source = "store.storeName", target = "storeName")
    @Mapping(target = "minPrice", ignore = true)
    @Mapping(target = "totalStock", ignore = true)
        // target - (source)
    ProductResponse toProductResponse(Product product);


}
