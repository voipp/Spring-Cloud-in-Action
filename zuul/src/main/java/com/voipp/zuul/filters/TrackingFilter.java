package com.voipp.zuul.filters;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackingFilter extends ZuulFilter {

    @Autowired
    private FilterUtils filterUtils;

    @Override
    public String filterType() {
        return filterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        if (isCorrelationIdPresent()) {
            System.err.println("correlationId found in" + TrackingFilter.class.getSimpleName() + " : " +
                    filterUtils.getCorrelationId());
        } else {
            filterUtils.setCorrelationId(generateCorrelationId());

            System.err.println("correlationId created in" + TrackingFilter.class.getSimpleName() + " : " +
                    filterUtils.getCorrelationId());
        }

        RequestContext ctx =
                RequestContext.getCurrentContext();
        System.err.println("Processing incoming request for " +
                ctx.getRequest().getRequestURI());
        return null;
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

    private boolean isCorrelationIdPresent() {
        if (filterUtils.getCorrelationId() !=null){
            return true;
        }
        return false;
    }
}
