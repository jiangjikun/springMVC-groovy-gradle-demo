package com.spiderdt.mars.exception

/**
 * @Title:
 * @Package com.spiderdt.cocacola.exception
 * @Description:
 * @author Kevin
 * @date 2017/3/28 11:52
 * @version V1.0
 */
class AccountLockException extends RuntimeException {
    static String errorCode = "F13"
    static String message = "Account is been locked for 1 hour."

}
