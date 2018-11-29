package com.voipp.organization.events;

import java.io.Serializable;

public class OrganizationChangeModel implements Serializable {
    private final String typeName;
    private final String action;
    private final String orgId;
    private final String correlationId;

    public OrganizationChangeModel(String typeName, String action, String orgId, String correlationId) {

        this.typeName = typeName;
        this.action = action;
        this.orgId = orgId;
        this.correlationId = correlationId;
    }
}
