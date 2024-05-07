package edu.guet.studentworkmanagementsystem.advice;

import edu.guet.studentworkmanagementsystem.common.BaseResponse;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import edu.guet.studentworkmanagementsystem.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;


@RestControllerAdvice
public class ExceptionControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    @ExceptionHandler(ServiceException.class)
    public <T> BaseResponse<T> serviceHandler(ServiceException serviceException) {
        return ResponseUtil.failure(serviceException);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public <T> BaseResponse<T> methodsNotSupportedHandler(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        return ResponseUtil.failure(ServiceExceptionEnum.METHOD_NOT_SUPPORT.getCode(),
                ServiceExceptionEnum.METHOD_NOT_SUPPORT.getMsg() + httpRequestMethodNotSupportedException.getMethod());
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public <T> BaseResponse<T> duplicateKeyExceptionHandler() {
        return ResponseUtil.failure(ServiceExceptionEnum.KEY_EXISTED);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public <T> BaseResponse<T> accessDeniedExceptionHandler() {
        return ResponseUtil.failure(ServiceExceptionEnum.INSUFFICIENT_PERMISSIONS);
    }
    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public <T> BaseResponse<T> authenticationExceptionHandler() {
        return ResponseUtil.failure(ServiceExceptionEnum.EMAIL_NO_PASSWORD_WRONG);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public <T> BaseResponse<T> usernameNotFoundExceptionHandler() {
        return ResponseUtil.failure(ServiceExceptionEnum.ACCOUNT_NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public <T> BaseResponse<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        return ResponseUtil.failure(ServiceExceptionEnum.METHOD_ARGUMENT_NOT_VALID.getCode(),
                ServiceExceptionEnum.METHOD_ARGUMENT_NOT_VALID.getMsg() + getAllErrorMessage(exception));
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public <T> BaseResponse<T> noResourceFoundException(NoResourceFoundException exception) {
        return ResponseUtil.failure(ServiceExceptionEnum.NOT_RESOURCE.getCode(), ServiceExceptionEnum.NOT_RESOURCE.getMsg() + exception.getMessage());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public <T> BaseResponse<T> dataIntegrityViolationException() {
        return ResponseUtil.failure(ServiceExceptionEnum.RELATE_FAILURE);
    }
    @ExceptionHandler(TransientDataAccessException.class)
    public <T> BaseResponse<T> queryCreationException() {
        return ResponseUtil.failure(ServiceExceptionEnum.DB_TIMEOUT);
    }
    @ExceptionHandler(NullPointerException.class)
    public <T> BaseResponse<T> nullPointerExceptionHandler(NullPointerException exception) {
        logger.error("出现空指针异常:", exception);
        return ResponseUtil.failure(ServiceExceptionEnum.NULL_POINTER);
    }
    @ExceptionHandler(Exception.class)
    public <T> BaseResponse<T> unknownExceptionHandler(Exception exception) {
        logger.error("出现未知异常:", exception);
        return ResponseUtil.failure(ServiceExceptionEnum.UNKNOWN_ERROR);
    }
    private String getAllErrorMessage(MethodArgumentNotValidException exception) {
        StringBuffer buffer = new StringBuffer();
        BindingResult result = exception.getBindingResult();
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            allErrors.forEach((objectError -> buffer.append(objectError.getDefaultMessage()).append(" ")));
        }
        return buffer.toString();
    }
}
