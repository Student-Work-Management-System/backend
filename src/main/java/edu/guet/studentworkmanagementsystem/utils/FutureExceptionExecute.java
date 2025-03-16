package edu.guet.studentworkmanagementsystem.utils;

import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureExceptionExecute <T> {
    private static final Logger log = LogManager.getLogger(FutureExceptionExecute.class);
    private final CompletableFuture<T> future;
    private FutureExceptionExecute(CompletableFuture<T> future) {
        this.future = future;
    }
    public T execute() {
        try{
            return future.get(3, TimeUnit.SECONDS);
        } catch (Exception exception) {
            Throwable cause = exception.getCause();
            log.error("FutureExceptionExecute find exception: ", cause);
            switch (cause) {
                case ServiceException serviceException ->
                        throw serviceException;
                case TimeoutException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_TIMEOUT);
                case InterruptedException ignored ->
                        throw new ServiceException(ServiceExceptionEnum.GET_RESOURCE_INTERRUPTED);
                default ->
                        throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
            }
        }
    }

    public static <T> FutureExceptionExecute<T> fromFuture(CompletableFuture<T> future) {
        return new FutureExceptionExecute<>(future);
    }
}
