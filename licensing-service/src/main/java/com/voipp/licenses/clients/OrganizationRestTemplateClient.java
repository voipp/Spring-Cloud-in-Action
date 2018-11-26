package com.voipp.licenses.clients;

import com.voipp.licenses.model.Organization;
import com.voipp.licenses.repository.OrganizationRedisRepository;
import com.voipp.licenses.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrganizationRedisRepository orgRedisRepo;

    private Organization checkRedisCache(String organizationId) {
        try {
            return orgRedisRepo.findOrganization(organizationId);
        } catch (Exception ex) {
            System.err.println("Error encountered while trying " +
                    "to retrieve organization {} check Redis Cache.Exception " + organizationId);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization org) {
        try {
            orgRedisRepo.saveOrganization(org);
        } catch (Exception ex) {

        }
    }

    public Organization getOrganization(String organizationId) {
        Organization org = checkRedisCache(organizationId);
        if (org != null) {

            return org;
        }

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://zuul/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null,
                        Organization.class,
                        organizationId);

        org = restExchange.getBody();

        if (org != null) {
            cacheOrganizationObject(org);
        }

        return org;
    }
}
