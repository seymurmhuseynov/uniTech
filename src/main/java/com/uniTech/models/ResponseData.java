package com.uniTech.models;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public class ResponseData<T> {

    private int statusCode = 200;
    private boolean success = Boolean.TRUE;
    private T body;

    public ResponseData() {
    }

    public ResponseData(T body) {
        this.body = body;
    }

    public ResponseData(int statusCode, boolean success, T body) {
        this.statusCode = statusCode;
        this.success = success;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ResponseData<T> setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public ResponseData<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public T getBody() {
        return body;
    }

    public ResponseData<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public static <T> ResponseData<T> ok() {
        return new ResponseData<>();
    }

    public static <T> ResponseData<T> ok(@Nullable T body) {
        return new ResponseData<>(body);
    }

    public static <T> ResponseData<T> error(@Nullable T body, HttpStatus httpStatus) {
        return new ResponseData<>(httpStatus.value(), false, body);
    }
}
