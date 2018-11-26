package com.voipp.licenses.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import com.voipp.licenses.clients.OrganizationFeignClient;
import com.voipp.licenses.config.ServiceConfig;
import com.voipp.licenses.model.License;
import com.voipp.licenses.model.Organization;
import com.voipp.licenses.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

    @Autowired
    OrganizationFeignClient organizationFeignClient;

    private Organization retrieveOrgInfo(String organizationId) {
        Organization organization = null;

        System.out.println("I am using the feign client");
        organization = organizationFeignClient.getOrganization(organizationId);

        return organization;
    }

    public License getLicense(String organizationId, String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization org = retrieveOrgInfo(organizationId);

        return license
                .withOrganizationName(org.getName())
                .withContactName(org.getContactName())
                .withContactEmail(org.getContactEmail())
                .withContactPhone(org.getContactPhone())
                .withComment(config.getExampleProperty());
    }

    @HystrixCommand(
            commandProperties =
                    {@HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "2000"),
                            @HystrixProperty(name = "coreSize", value = "30"),
                            @HystrixProperty(name = "maxQueueSize", value = "10")
                    },
            fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licenseByOrgThreadPool"
    )
    public List<License> getLicensesByOrg(String organizationId) throws InterruptedException {
        Thread.sleep(3_000);

        return licenseRepository.findByOrganizationId(organizationId);
    }

    private List<License> buildFallbackLicenseList(String organizationId) {
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId(organizationId)
                .withProductName(
                        "Sorry no licensing information currently available");
        fallbackList.add(license);
        return fallbackList;
    }

    public void saveLicense(License license) {
        license.withId(UUID.randomUUID().toString());

        licenseRepository.save(license);

    }

    public void updateLicense(License license) {
        licenseRepository.save(license);
    }

    public void deleteLicense(License license) {
        licenseRepository.delete(license.getLicenseId());
    }

}
