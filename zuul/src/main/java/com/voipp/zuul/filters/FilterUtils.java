package com.voipp.zuul.filters;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

@Component
public class FilterUtils {
    public static final String CORRELATION_ID = "correlationid";
    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";

    public String getCorrelationId() {
        RequestContext ctx = RequestContext.getCurrentContext();

        String correlationId = ctx.getRequest().getHeader(CORRELATION_ID);

        if (correlationId != null)
            return correlationId;
        else return ctx.getZuulRequestHeaders().get(CORRELATION_ID);
    }

    public void setCorrelationId(String correlationId){
        RequestContext ctx =
                RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(CORRELATION_ID, correlationId);
    }
}
