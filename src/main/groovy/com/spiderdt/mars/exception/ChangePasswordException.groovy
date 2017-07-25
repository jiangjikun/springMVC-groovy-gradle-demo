package com.spiderdt.mars.exception

/**
 * @Title:
 * @Package com.spiderdt.cocacola.exception
 * @Description:
 * @author Kevin
 * @date 2017/3/28 13:42
 * @version V1.0
 */
class ChangePasswordException extends RuntimeException{

    final String status
    final String message
    final String errorCode

    ChangePasswordException(String status, String message, String errorCode) {
        this.status = status
        this.message = message
        this.errorCode = errorCode
    }
}
