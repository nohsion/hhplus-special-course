package com.hhplus.hhplus_special_course.global.common.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    @JsonProperty(value = "result")
    private ResultCode result;

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "data")
    private T data;

    public enum ResultCode {
        SUCCESS,
        ERROR
    }

    private ApiResponse() {
    }

    private ApiResponse(ResultCode result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ofSuccess(T data) {
        return new ApiResponse<>(ResultCode.SUCCESS, "", data);
    }
}
