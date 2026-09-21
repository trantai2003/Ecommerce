package com.dev.backend.service.impl;

import com.dev.backend.dto.request.ProductCreateRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.ProductResponse;
import com.dev.backend.dto.response.StoreResponse;
import com.dev.backend.entities.*;
import com.dev.backend.repository.*;
import com.dev.backend.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductVariantRepository variantRepository, StoreRepository storeRepository, UserRepository userRepository, CategoryProductRepository categoryProductRepository, ColorRepository colorRepository, SizeRepository sizeRepository) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.categoryProductRepository = categoryProductRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
    }


    @Override
    public BaseResponse<ProductResponse> create(UUID currentUserId, ProductCreateRequest request) {
        BaseResponse<StoreResponse> response = new BaseResponse<>();
        User user = userRepository.findById(currentUserId).orElse(null);
        if(user == null){
            response.setCode(404);
            response.setMsg("Người dùng không tồn tại");
        }
        Store store = storeRepository.findById(request.getStoreId()).orElse(null);
        if(store == null){
            response.setCode(404);
            response.setMsg("Cửa hàng không tồn tại");
        }
        CategoryProduct categoryProduct = categoryProductRepository.findById(request.getCategoryId()).orElse(null);
        if(categoryProduct == null){
            response.setCode(404);
            response.setMsg("Danh mục sản phẩm không tồn tại");
        }
//        Color color = colorRepository.findById(request.getVariants());
//        Size size = sizeRepository.findById()
        //Check size và color khó quá
        return null;
    }
}
