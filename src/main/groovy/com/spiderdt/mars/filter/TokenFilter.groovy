package com.spiderdt.mars.filter

import com.spiderdt.mars.service.HttpClientService
import com.spiderdt.mars.util.Slog
import com.spiderdt.mars.util.Sredis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenFilter extends OncePerRequestFilter {
    @Autowired
    Slog slog

    @Autowired
    HttpClientService httpClientService

    @Autowired
    Sredis sredis

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String token = request.getHeader("token")
        slog.info("--------------request header token is: $token ----------------")

        // token 格式： ad7feade-1ed1-49b4-82af-755e2115f88c2

        // 如果请求包含了 token , 但是 token 的格式不正确, 返回 401 token invalid
        if (token && !token.matches('[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}')) {
            slog.info("------------------$token is invalid ---------------------")
            response.sendError(401, "token invalid")
        } else {
            // 如果 token 不为空
            if (token) {

                if (httpClientService.validateTokenByChain(token)) {
                    def data_source = null
                    try {
                        data_source = sredis.getString("data_source:"+token)
                    } catch (Exception e) {
                    }
                    if (data_source == null) {
                        def username = httpClientService.getUserInfoByTokenByChain(token).data.userId
                        data_source = httpClientService.getUserAllInfoByChain(token, username).data.dataSource
                        def user_reids_key = "username:"+token
                        def data_source_reids_key = "data_source:"+token
                        sredis.addString(user_reids_key,username,86400)
                        sredis.addString(data_source_reids_key,data_source,86400)
                    }
                    // 更新 token 过期时间
                    slog.info("testing access chain api")
//                    httpClientService.updateTokenLiveTimeByChain(token)
                } else {
                    response.setHeader("Access-Control-Allow-Origin", "*");
                    response.setHeader("Access-Control-Max-Age", "3600");
                    response.setStatus(401);

                    return
                }
            }
            filterChain.doFilter(request, response)
        }
    }
}
