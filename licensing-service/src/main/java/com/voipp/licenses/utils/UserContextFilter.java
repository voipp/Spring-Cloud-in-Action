package com.voipp.licenses.utils;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class UserContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("start UserContextFilter:doFilter()");

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        UserContext ctx = UserContextHolder.getContext();

        String correlationId = request.getHeader(UserContext.CORRELATION_ID);

        ctx.setCorrelationId(correlationId);

        ctx.setUserId(request.getHeader(UserContext.USER_ID));

        ctx.setAuthToken(request.getHeader(UserContext.AUTH_TOKEN));

        ctx.setOrgId(request.getHeader(UserContext.ORG_ID));

        System.out.println("continue filtering. Correlation id= " + correlationId);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
