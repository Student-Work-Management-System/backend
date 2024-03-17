package edu.guet.studentworkmanagementsystem.exception;

final public class ServiceException extends RuntimeException {
    private int code;
    private String msg;
    public ServiceException(ServiceExceptionEnum serviceExceptionEnum) {
        this.code = serviceExceptionEnum.getCode();
        this.msg = serviceExceptionEnum.getMsg();
    }
    public ServiceException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
