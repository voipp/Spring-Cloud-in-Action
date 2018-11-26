package com.voipp.organization.utils;

import com.google.common.collect.Maps;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Component
public class UserContextInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("UserContextInterceptor:apply()");

        Map<String, Collection<String>> currentHeaders = Maps.newHashMap(requestTemplate.headers());

        UserContext ctx = UserContextHolder.getContext();

        currentHeaders.put(ctx.CORRELATION_ID, Collections.singleton(ctx.getCorrelationId()));
        currentHeaders.put(ctx.AUTH_TOKEN, Collections.singleton(ctx.getAuthToken()));


        requestTemplate.headers(currentHeaders);
    }
}
