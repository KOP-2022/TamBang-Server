package com.example.tambang.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResponseDto<T> {
    private final boolean success;
    private final T data;

    @Builder
    public CommonResponseDto(boolean success, T data){
        this.success = success;
        this.data = data;
    }
}
