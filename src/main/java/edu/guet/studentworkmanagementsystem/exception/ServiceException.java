package edu.guet.studentworkmanagementsystem.exception;

import lombok.Getter;

@Getter
final public class ServiceException extends RuntimeException {
    private final int code;
    private final String msg;
    public ServiceException(ServiceExceptionEnum serviceExceptionEnum) {
        this.code = serviceExceptionEnum.getCode();
        this.msg = serviceExceptionEnum.getMsg();
    }
    public ServiceException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
