package com.dev.backend.dto.response;

import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Setter
@Getter
public class BaseResponse<T> {
    private int code;
    private String msg;
    private T data;
}
