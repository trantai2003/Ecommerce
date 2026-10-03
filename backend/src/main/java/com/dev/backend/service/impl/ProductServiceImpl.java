package com.dev.backend.service.impl;

import com.dev.backend.dto.request.ProductCreateRequest;
import com.dev.backend.dto.request.ProductUpdateRequest;
import com.dev.backend.dto.request.ProductVariantRequest;
import com.dev.backend.dto.request.ProductVariantUpdateRequest;
import com.dev.backend.dto.response.*;
import com.dev.backend.entities.*;
import com.dev.backend.mapper.ProductMapper;
import com.dev.backend.mapper.ProductVariantMapper;
import com.dev.backend.repository.*;
import com.dev.backend.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final ProductMapper productMapper;
    private final ProductVariantMapper productVariantMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductVariantRepository variantRepository,
                              StoreRepository storeRepository,
                              UserRepository userRepository,
                              CategoryProductRepository categoryProductRepository,
                              ColorRepository colorRepository,
                              SizeRepository sizeRepository,
                              ProductMapper productMapper,
                              ProductVariantMapper productVariantMapper) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.categoryProductRepository = categoryProductRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.productMapper = productMapper;
        this.productVariantMapper = productVariantMapper;
    }


    @Override
    public Void create_v1(UUID currentUserId, ProductCreateRequest request) {
        //Tao moi product
        boolean existProductName = productRepository.existsByNameContainingIgnoreCase(request.getName());
        if (existProductName) {
            System.out.println("Tên sản phẩm đã tồn tại");
            System.out.println(400);
        }
        CategoryProduct categoryProduct = categoryProductRepository.findById(request.getCategoryId()).orElse(null);
        if (categoryProduct == null) {
            System.out.println(400);
            System.out.println("Danh mục sản phẩm không tồn tại");
        }
        Store store = storeRepository.findById(request.getStoreId()).orElse(null);
        if (store == null) {
            System.out.println(400);
            System.out.println("Cửa hàng không tồn tại");
        }
        Product product = new Product();
        UUID newId = UUID.randomUUID();
        product.setId(newId);
        product.setName(request.getName());
        product.setImage(request.getImage());
        product.setGender(request.getGender());
        product.setDescription(request.getDescription());
        product.setCategoryProduct(categoryProduct);
        product.setStore(store);
        product = productRepository.save(product);

        List<ProductVariant> variants = new ArrayList<>();
        //tao lan luot tung product variant tuong ung
        for (ProductVariantRequest productVariantRequest : request.getVariants()) {
            Color color = colorRepository.findById(productVariantRequest.getColorId()).orElse(null);
            if (color == null) {
                System.out.println(400);
                System.out.println("Màu không tồn tại");
            }
            Size size = sizeRepository.findById(productVariantRequest.getSizeId()).orElse(null);
            if (size == null) {
                System.out.println(400);
                System.out.println("Size không tồn tại");
            }

            ProductVariant productVariant = new ProductVariant();
            productVariant.setProduct(product);
            productVariant.setColor(color);
            productVariant.setSize(size);
            productVariant.setSku(productVariantRequest.getSku());
            productVariant.setPrice(productVariantRequest.getPrice());
            productVariant.setStock(productVariantRequest.getStock());
            variants.add(productVariant);
        }
        variantRepository.saveAll(variants);

        return null;
    }

    @Override
    public BaseResponse<ProductResponse> create_v2(UUID currentUserId, ProductCreateRequest request) {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        //Tao moi product
        boolean existProductName = productRepository.existsByNameContainingIgnoreCase(request.getName());
        if (existProductName) {
            response.setMsg("Tên sản phẩm đã tồn tại");
            response.setCode(400);
            return response;
        }
        CategoryProduct categoryProduct = categoryProductRepository.findById(request.getCategoryId()).orElse(null);
        if (categoryProduct == null) {
            response.setMsg("Danh mục sản phẩm không tồn tại");
            response.setCode(400);
            return response;

        }
        Store store = storeRepository.findById(request.getStoreId()).orElse(null);
        if (store == null) {
            response.setMsg("Cửa hàng không tồn tại");
            response.setCode(400);
            return response;

        }
        //Tạo product
        Product product = new Product();
//        UUID newId = UUID.randomUUID();
//        product.setId(newId);
        product.setName(request.getName());
        product.setImage(request.getImage());
        product.setGender(request.getGender());
        product.setDescription(request.getDescription());
        product.setCategoryProduct(categoryProduct);
        product.setStore(store);
        productRepository.save(product);

        //Chuyển từ product -> productResponse( vì kiểu trả về là BaseResponse<ProductResponse>)
        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(product.getName());
        productResponse.setImage(product.getImage());
        productResponse.setGender(product.getGender());
        productResponse.setDescription(product.getDescription());
        productResponse.setCategoryName(product.getCategoryProduct().getCategoryName());
        productResponse.setStoreName(product.getStore().getStoreName());


        List<ProductVariantResponse> variantResponses = new ArrayList<>();

        List<ProductVariant> variants = new ArrayList<>();
        //tao lan luot tung product variant tuong ung
        for (ProductVariantRequest productVariantRequest : request.getVariants()) {
            Color color = colorRepository.findById(productVariantRequest.getColorId()).orElse(null);
            if (color == null) {
                response.setMsg("Màu không tồn tại");
                response.setCode(400);
            }
            Size size = sizeRepository.findById(productVariantRequest.getSizeId()).orElse(null);
            if (size == null) {
                response.setMsg("Size không tồn tại");
                response.setCode(400);
            }

            //Tạo productVariant
            ProductVariant productVariant = new ProductVariant();
            productVariant.setProduct(product);
            productVariant.setColor(color);
            productVariant.setSize(size);
            productVariant.setSku(productVariantRequest.getSku());
            productVariant.setPrice(productVariantRequest.getPrice());
            productVariant.setStock(productVariantRequest.getStock());
            //Thêm productVariant vào danh sách
            variants.add(productVariant);

            //Chuyển từ productVariant -> productVariantResponse: vì kiểu dữ liệu trả về là
            // 1 BaseResponse<ProductResponse>
            ProductVariantResponse productVariantResponse = new ProductVariantResponse();
            productVariantResponse.setColorName(productVariant.getColor().getColorName());
            productVariantResponse.setSizeName(productVariant.getSize().getSizeName());
            productVariantResponse.setSku(productVariant.getSku());
            productVariantResponse.setPrice(productVariant.getPrice());
            productVariantResponse.setStock(productVariant.getStock());
            variantResponses.add(productVariantResponse);

        }
        variantRepository.saveAll(variants);
        productResponse.setVariants(variantResponses);
        response.setData(productResponse);
        response.setCode(200);
        response.setMsg("Success");
        return response;
    }

    @Override
    public BaseResponse<ProductResponse> create_v3(UUID currentUserId, ProductCreateRequest request) {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        //Tao moi product
        boolean existProductName = productRepository.existsByNameContainingIgnoreCase(request.getName());
        if (existProductName) {
            response.setMsg("Tên sản phẩm đã tồn tại");
            response.setCode(400);
            return response;
        }
        CategoryProduct categoryProduct = categoryProductRepository.findById(request.getCategoryId()).orElse(null);
        if (categoryProduct == null) {
            response.setMsg("Danh mục sản phẩm không tồn tại");
            response.setCode(400);
            return response;

        }
        Store store = storeRepository.findById(request.getStoreId()).orElse(null);
        if (store == null) {
            response.setMsg("Cửa hàng không tồn tại");
            response.setCode(400);
            return response;

        }
        //Chuyển từ ProductCreateRequest -> entity product
        Product product = productMapper.toProduct(request);
//        product.setName(request.getName());
//        product.setImage(request.getImage());
//        product.setGender(request.getGender());
//        product.setDescription(request.getDescription());
//        product.setCategoryProduct(categoryProduct);
//        product.setStore(store);
        productRepository.save(product);

        List<ProductVariantResponse> variantResponses = new ArrayList<>();

        List<ProductVariant> variants = new ArrayList<>();
        //tao lan luot tung product variant tuong ung
        for (ProductVariantRequest productVariantRequest : request.getVariants()) {
            Color color = colorRepository.findById(productVariantRequest.getColorId()).orElse(null);
            if (color == null) {
                response.setMsg("Màu không tồn tại");
                response.setCode(400);
            }
            Size size = sizeRepository.findById(productVariantRequest.getSizeId()).orElse(null);
            if (size == null) {
                response.setMsg("Size không tồn tại");
                response.setCode(400);
            }

            //Tạo productVariant
            ProductVariant productVariant = productVariantMapper.toProductVariant(productVariantRequest);
            productVariant.setProduct(product);
//            productVariant.setColor(color);
//            productVariant.setSize(size);
//            productVariant.setSku(productVariantRequest.getSku());
//            productVariant.setPrice(productVariantRequest.getPrice());
//            productVariant.setStock(productVariantRequest.getStock());

            //Thêm productVariant vào danh sách
            variants.add(productVariant);

            //Chuyển từ productVariant -> productVariantResponse: vì kiểu dữ liệu trả về là 1 BaseResponse<ProductResponse>
            ProductVariantResponse productVariantResponse = new ProductVariantResponse();
//            productVariantResponse.setColorName(productVariant.getColor().getColorName());
//            productVariantResponse.setSizeName(productVariant.getSize().getSizeName());
//            productVariantResponse.setSku(productVariant.getSku());
//            productVariantResponse.setPrice(productVariant.getPrice());
//            productVariantResponse.setStock(productVariant.getStock());
            productVariantResponse = productVariantMapper.toProductVariantResponse(productVariant);
            variantResponses.add(productVariantResponse);

        }
        ProductResponse productResponse = productMapper.toProductResponse(product);
        productResponse.setVariants(variantResponses);
        variantRepository.saveAll(variants);
        response.setData(productResponse);
        response.setCode(200);
        response.setMsg("Success");
        return response;
    }

    @Override
    public BaseResponse<ProductUpdateResponse> update_v1(UUID productId, ProductUpdateRequest updateRequest) {
        BaseResponse<ProductUpdateResponse> response = new BaseResponse<>();

        CategoryProduct categoryProduct = categoryProductRepository.findById(updateRequest.getCategoryId()).orElse(null);
        if (categoryProduct == null) {
            response.setMsg("Danh mục sản phẩm không tồn tại");
            response.setCode(400);
            return response;

        }
        Store store = storeRepository.findById(updateRequest.getStoreId()).orElse(null);
        if (store == null) {
            response.setMsg("Cửa hàng không tồn tại");
            response.setCode(400);
            return response;

        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            response.setMsg("Id sản phẩm không tồn tại");
            response.setCode(404);
            return response;
        }

        if (updateRequest.getVariantUpdateRequests() == null) {
            response.setMsg("Danh sách biến thể không được để trống");
            response.setCode(400);
            return response;
        }


        //ProductUpdateRequest -> Product
        product.setName(updateRequest.getName());
        product.setImage(updateRequest.getImage());
        product.setGender(updateRequest.getGender());
        product.setDescription(updateRequest.getDescription());
        product.setCategoryProduct(categoryProduct);
        product.setStore(store);
        productRepository.save(product);

        //Product -> ProductUpdateResponse
        ProductUpdateResponse updateResponse = new ProductUpdateResponse();
        updateResponse.setName(product.getName());
        updateResponse.setImage(product.getImage());
        updateResponse.setGender(product.getGender());
        updateResponse.setDescription(product.getDescription());
        updateResponse.setCategoryName(product.getCategoryProduct().getCategoryName());
        updateResponse.setStoreName(product.getStore().getStoreName());
        updateResponse.setUpdatedDate(LocalDateTime.now());


        List<ProductVariant> productVariants = new ArrayList<>();
        List<ProductVariantUpdateResponse> updateVariantResponses = new ArrayList<>();

        for (ProductVariantUpdateRequest productVariantUpdateRequest : updateRequest.getVariantUpdateRequests()) {
            Color color = colorRepository.findById(productVariantUpdateRequest.getColorId()).orElse(null);
            if (color == null) {
                response.setMsg("Màu không tồn tại");
                response.setCode(400);
                return response;
            }
            Size size = sizeRepository.findById(productVariantUpdateRequest.getSizeId()).orElse(null);
            if (size == null) {
                response.setMsg("Size không tồn tại");
                response.setCode(400);
                return response;
            }

            //ProductVariantUpdateRequest -> ProductVariant
            ProductVariant productVariant = variantRepository.findById(productVariantUpdateRequest.getVariantId()).orElse(null);
            if (productVariant == null) {
                response.setMsg("Biến thể không tồn tại");
                response.setCode(404);
                return response;
            }
            if (!productVariant.getProduct().getId().equals(productId)) {
                response.setMsg("Biến thể không thuộc sản phẩm này");
                response.setCode(400);
                return response;
            }
//            productVariant.setProduct(product);
            productVariant.setColor(color);
            productVariant.setSize(size);
            productVariant.setSku(productVariantUpdateRequest.getSku());
            productVariant.setPrice(productVariantUpdateRequest.getPrice());
            productVariant.setStock(productVariantUpdateRequest.getStock());
            productVariants.add(productVariant);

            //ProductVariant -> ProductVariantUpdateResponse
            ProductVariantUpdateResponse variantUpdateResponse = new ProductVariantUpdateResponse();
            variantUpdateResponse.setColorName(productVariant.getColor().getColorName());
            variantUpdateResponse.setSizeName(productVariant.getSize().getSizeName());
            variantUpdateResponse.setSku(productVariant.getSku());
            variantUpdateResponse.setPrice(productVariant.getPrice());
            variantUpdateResponse.setStock(productVariant.getStock());
            variantUpdateResponse.setUpdatedDate(LocalDateTime.now());
            updateVariantResponses.add(variantUpdateResponse);

        }
        variantRepository.saveAll(productVariants);
        updateResponse.setVariantUpdateResponses(updateVariantResponses);
        response.setData(updateResponse);
        response.setCode(200);
        response.setMsg("Success");
        return response;
    }

    @Override
    public BaseResponse<ProductUpdateResponse> update_v2(UUID productId, ProductUpdateRequest updateRequest) {
        BaseResponse<ProductUpdateResponse> response = new BaseResponse<>();

        CategoryProduct categoryProduct = categoryProductRepository.findById(updateRequest.getCategoryId()).orElse(null);
        if (categoryProduct == null) {
            response.setMsg("Danh mục sản phẩm không tồn tại");
            response.setCode(400);
            return response;

        }
        Store store = storeRepository.findById(updateRequest.getStoreId()).orElse(null);
        if (store == null) {
            response.setMsg("Cửa hàng không tồn tại");
            response.setCode(400);
            return response;

        }

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            response.setMsg("Id sản phẩm không tồn tại");
            response.setCode(404);
            return response;
        }

        if (updateRequest.getVariantUpdateRequests() == null) {
            response.setMsg("Biến thể sản phẩm không được để trống");
            response.setCode(404);
            return response;
        }

        //ProductUpdateRequest -> Product
        productMapper.requestUpdateProductToProduct(updateRequest);
        productRepository.save(product);

        List<ProductVariant> productVariants = new ArrayList<>();
        List<ProductVariantUpdateResponse> updateVariantResponses = new ArrayList<>();

        for (ProductVariantUpdateRequest productVariantUpdateRequest : updateRequest.getVariantUpdateRequests()) {
            Color color = colorRepository.findById(productVariantUpdateRequest.getColorId()).orElse(null);
            if (color == null) {
                response.setMsg("Màu không tồn tại");
                response.setCode(400);
                return response;
            }
            Size size = sizeRepository.findById(productVariantUpdateRequest.getSizeId()).orElse(null);
            if (size == null) {
                response.setMsg("Size không tồn tại");
                response.setCode(400);
                return response;
            }
            ProductVariant productVariant = variantRepository.findById(productVariantUpdateRequest.getVariantId()).orElse(null);
            if (!productVariant.getProduct().getId().equals(productId)) {
                response.setMsg("Biến thể không thuộc sản phẩm này");
                response.setCode(400);
                return response;
            }

            //ProductVariantUpdateRequest -> ProductVariant
            productVariant = productVariantMapper.updateProductVariantToProductVariant(productVariantUpdateRequest);

//            productVariant.setProduct(product);
            productVariants.add(productVariant);

            //ProductVariant -> ProductVariantUpdateResponse
            ProductVariantUpdateResponse variantUpdateResponse = productVariantMapper.productVariantToProductVariantResponse(productVariant);
            updateVariantResponses.add(variantUpdateResponse);
        }
        ProductUpdateResponse productUpdateResponse = productMapper.productToProductUpdateResponse(product);
        productUpdateResponse.setVariantUpdateResponses(updateVariantResponses);
        variantRepository.saveAll(productVariants);
        response.setData(productUpdateResponse);
        response.setCode(200);
        response.setMsg("Success");
        return response;
    }
}
