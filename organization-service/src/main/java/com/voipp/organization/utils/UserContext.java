package com.voipp.organization.utils;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
    public static final String USER_ID = "userId";
    public static final String AUTH_TOKEN = "authToken";
    public static final String ORG_ID = "orgId";
    public static final String CORRELATION_ID = "correlationId";
    private String correlationId;
    private String userId;
    private String authToken;
    private String orgId;

    public void setCorrelationId(String request) {

        this.correlationId = request;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }

    public void setAuthToken(String token) {

        this.authToken = token;
    }

    public void setOrgId(String orgId) {

        this.orgId = orgId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getOrgId() {
        return orgId;
    }
}
