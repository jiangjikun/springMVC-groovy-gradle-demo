package com.spiderdt.mars.exception
/**
 * @Title:
 * @Package com.spiderdt.cocacola.exception
 * @Description:
 * @author Kevin
 * @date 2017/3/23 10:19
 * @version V1.0
 */
class CommonException extends RuntimeException {


    String message
    String errorCode

    CommonException(String message, errorCode) {
        super()
        this.message = message
        this.errorCode = errorCode
    }
}
