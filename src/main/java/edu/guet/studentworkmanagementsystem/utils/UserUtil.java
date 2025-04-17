package edu.guet.studentworkmanagementsystem.utils;

import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;

public final class UserUtil {
    public static String createPassword(String idNumber) {
        if (idNumber.isEmpty())
            throw new ServiceException(ServiceExceptionEnum.OPERATE_ERROR);
        return idNumber.length() >= 6 ? idNumber.substring(0, 6) : idNumber;
    }
}
