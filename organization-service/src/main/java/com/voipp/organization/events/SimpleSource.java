package com.voipp.organization.events;

import com.voipp.organization.utils.UserContext;
import com.voipp.organization.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleSource {
    private Source source;
    private static final Logger logger =
            LoggerFactory.getLogger(SimpleSource.class);

    @Autowired
    public SimpleSource(Source source) {
        this.source = source;
    }

    public void publishOrgChange(String action, String orgId) {
        logger.debug("Sending Kafka message {} for Organization Id: {}", action, orgId);

        UserContext ctx = UserContextHolder.getContext();

        OrganizationChangeModel change = new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action,
                orgId,
                ctx.getCorrelationId());

        source.output()
                .send(MessageBuilder.withPayload("HELLO_WORLD").build());
    }
}
