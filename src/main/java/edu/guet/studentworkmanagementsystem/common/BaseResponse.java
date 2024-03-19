package edu.guet.studentworkmanagementsystem.common;

import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {
    private final int code;
    private final String message;
    private final T data;
    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }
    public BaseResponse(ServiceExceptionEnum serviceExceptionEnum) {
        this.code = serviceExceptionEnum.getCode();
        this.message = serviceExceptionEnum.getMsg();
        this.data = null;
    }
    public BaseResponse(ServiceException serviceException) {
        this.code = serviceException.getCode();
        this.message = serviceException.getMsg();
        this.data = null;
    }
    public BaseResponse(T data) {
        this.code = 200;
        this.message = "success";
        this.data = data;
    }
    public BaseResponse() {
        this.code = 200;
        this.message = "success";
        this.data = null;
    }
}
