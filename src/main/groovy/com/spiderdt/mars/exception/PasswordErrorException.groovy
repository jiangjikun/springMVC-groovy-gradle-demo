package com.spiderdt.mars.exception
/**
 * @Title:
 * @Package com.spiderdt.cocacola.exception
 * @Description:
 * @author Kevin
 * @date 2017/3/28 11:36
 * @version V1.0
 */
class PasswordErrorException  extends RuntimeException {

    final static String errorCode = "F16"
    PasswordErrorException(String message) {
        super(message)
    }
}
