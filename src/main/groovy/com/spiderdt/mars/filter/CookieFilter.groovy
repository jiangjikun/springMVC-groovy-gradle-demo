package com.spiderdt.mars.filter

import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class CookieFilter extends OncePerRequestFilter  {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //response.addHeader("Set-Cookie", "secure; HttpOnly")
        filterChain.doFilter(request, response)


    }
}
