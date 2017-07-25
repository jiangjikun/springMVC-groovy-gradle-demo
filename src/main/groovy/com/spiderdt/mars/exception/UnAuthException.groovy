package com.spiderdt.mars.exception

import org.springframework.stereotype.Component

/**
 * @Title:
 * @Package com.spiderdt.cocacola.exception
 * @Description:
 * @author ranran
 * @date 2017/3/22 11:27
 * @version V1.0
 */
@Component
class UnAuthException extends RuntimeException {

    static String message = "unAuth"

    UnAuthException() {
        super()
    }
}
