package com.spiderdt.mars.filter

import org.springframework.web.filter.ServletContextRequestLoggingFilter

import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by fivebit on 2017/6/14.
 */
class TimeFilter extends  ServletContextRequestLoggingFilter{
    private static Logger log = LoggerFactory.getLogger(TimeFilter.class);
    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        String requestid = "";
        try {
            requestid = null == request.getHeader("X-Request-ID") ?
                    UUID.randomUUID().toString().replace("-", "").substring(0, 15)
                    : request.getHeader("X-Request-ID");
        }catch (Exception ee){
            requestid = UUID.randomUUID().toString().replace("-", "").substring(0, 15)
        }
        String log_info = requestid+" "+request.getMethod()+" "+request.getServletPath()+" request begin";
        log.info(log_info);
    }

    /**
     * Writes a log message after the request is processed.
     */
    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        String requestid = "";
        try {
            requestid = null == request.getHeader("X-Request-ID") ?
                    UUID.randomUUID().toString().replace("-", "").substring(0, 15)
                    : request.getHeader("X-Request-ID");
        }catch (Exception ee){
            requestid = UUID.randomUUID().toString().replace("-", "").substring(0, 15)
        }
        String log_info = requestid +" "+request.getMethod()+" "+request.getServletPath()+" request end";
        log.info(log_info);
    }

}
