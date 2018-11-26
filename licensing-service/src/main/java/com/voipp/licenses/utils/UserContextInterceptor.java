package com.voipp.licenses.utils;

import com.google.common.collect.Maps;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
