package com.dev.backend.service.impl;

import com.dev.backend.dto.request.StoreRequest;
import com.dev.backend.dto.response.BaseResponse;
import com.dev.backend.dto.response.StoreResponse;
import com.dev.backend.entities.Store;
import com.dev.backend.entities.User;
import com.dev.backend.mapper.StoreMapper;
import com.dev.backend.repository.RoleRepository;
import com.dev.backend.repository.StoreRepository;
import com.dev.backend.repository.UserRepository;
import com.dev.backend.security.SecurityUtils;
import com.dev.backend.service.StoreService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StoreMapper storeMapper;

    public StoreServiceImpl(StoreRepository storeRepository, UserRepository userRepository, RoleRepository roleRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.storeMapper = storeMapper;
    }

    @Override
    public BaseResponse<StoreResponse> create(UUID currentUserId, StoreRequest request) {
        BaseResponse<StoreResponse> response = new BaseResponse<>();
        User user = userRepository.findById(currentUserId).orElse(null);
        if (user == null) {
            response.setCode(404);
            response.setMsg("Người dùng không tồn tại");
            return response;
        }

        if (storeRepository.existsByStoreNameIgnoreCase(request.getStoreName())) {
            response.setCode(404);
            response.setMsg(" Tên cửa hàng đã tồn tại");
            return response;
        }
        User owner = userRepository.findById(request.getOwnerId()).orElse(null);
        if (owner == null) {
            response.setCode(404);
            response.setMsg("Người dùng đã tồn tại");
            return response;
        }
        //mapper: req-> Store
        Store store = new Store();
        store.setStoreImage(request.getStoreImage());
        store.setStoreName(request.getStoreName());
        store.setDescription(request.getStoreDescriptions());
        store.setOwner(owner);
        storeRepository.save(store);
        //mapper: Store ->  BaseResponse<StoreResponse>
        StoreResponse storeResponse = new StoreResponse();
        storeResponse.setStoreImage(store.getStoreImage());
        storeResponse.setStoreName(store.getStoreName());
        store.setDescription(store.getDescription());
        store.setOwner(store.getOwner());
        response.setMsg("Tạo cửa hàng thành công");
        response.setCode(200);
        response.setData(storeResponse);
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StoreResponse> getListStore(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> stores = storeRepository.findAll(pageable);
        if (search != null && !search.isEmpty()) {
            stores = storeRepository.findByStoreNameContainingIgnoreCase(search, pageable);
        }
        return stores.map(storeMapper::storeResponse);
    }

    @Transactional
    @Override
    public BaseResponse<StoreResponse> update(UUID storeId, StoreRequest request) {
        BaseResponse<StoreResponse> response = new BaseResponse<>();

        // 1. Lay nguoi dang dang nhap tu JWT token (khong nhan tu client)
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            response.setCode(401);
            response.setMsg("Bạn chưa đăng nhập");
            return response;
        }

        // 2. Cua hang ton tai?
        Store store = storeRepository.findById(storeId).orElse(null);
        if (store == null) {
            response.setCode(404);
            response.setMsg("Cửa hàng không tồn tại");
            return response;
        }

        // 3. Nguoi dang nhap co phai chu cua hang?
        if (store.getOwner() == null || !store.getOwner().getId().equals(currentUserId)) {
            response.setCode(403);
            response.setMsg("Bạn không phải chủ cửa hàng này");
            return response;
        }

        // 4. Ten hop le, khong trung voi cua hang khac
//        String storeName = request.getStoreName() == null ? "   " : request.getStoreName().trim();
        if (request.getStoreName() == null && request.getStoreName().isEmpty()) {
            response.setCode(400);
            response.setMsg("Tên cửa hàng không được để trống");
            return response;
        }
        if (storeRepository.existsByStoreNameIgnoreCaseAndIdNot(request.getStoreName(), storeId)) {
            response.setCode(409);
            response.setMsg("Tên cửa hàng đã tồn tại");
            return response;
        }

        // 5. Cap nhat (khong doi owner). @Transactional -> Hibernate tu UPDATE khi ket thuc method
        store.setStoreName(request.getStoreName());
        store.setDescription(request.getStoreDescriptions());
        store.setStoreImage(request.getStoreImage());
        storeRepository.save(store);

        response.setCode(200);
        response.setMsg("Cập nhật cửa hàng thành công");
        response.setData(storeMapper.storeResponse(store));
        return response;
    }

    @Override
    public BaseResponse<Void> delete(UUID storeId, UUID currentId) {
        BaseResponse<StoreResponse> response = new BaseResponse<>();
        if (!userRepository.existsById(currentId)) {
            response.setCode(404);
            response.setMsg("Người dùng không tồn tại");
        }

        Store store = storeRepository.findById(storeId).orElse(null);
        if (store == null) {
            response.setCode(404);
            response.setMsg("Cửa hàng không tồn tại");
        }
        storeRepository.delete(store);
        response.setCode(200);
        response.setMsg("Xoá cửa hàng thành công");
        return null;
    }

}
