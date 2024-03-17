package edu.guet.studentworkmanagementsystem.utils;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

final public class ResponseUtil {
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>();
    }
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data);
    }
    public static <T> BaseResponse<T> failure(ServiceException serviceException) {
        return new BaseResponse<>(serviceException);
    }
    public static <T> BaseResponse<T> failure(ServiceExceptionEnum serviceExceptionEnum) {
        return new BaseResponse<>(serviceExceptionEnum);
    }
    public static <T> BaseResponse<T> failure(int code, String message) {
        return new BaseResponse<>(code, message);
    }
    public static <T> void failure(HttpServletResponse response, ServiceExceptionEnum serviceExceptionEnum) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        BaseResponse<Object> baseResponse = new BaseResponse<>(serviceExceptionEnum);
        JsonUtil.mapper.writeValue(response.getOutputStream(), baseResponse);
    }
}
