package com.ovidius.minecraft.client.services;

import com.ovidius.minecraft.client.dto.NetworkStatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class XorealisBridgeClient {

    private static final Logger log = LoggerFactory.getLogger(XorealisBridgeClient.class);

    private final RestTemplate restTemplate;

    @Value("${xorealis.bridge.api.base-url}")
    private String apiBaseUrl;

    public XorealisBridgeClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Optional<NetworkStatusDto> getNetworkStatus() {
        String url = apiBaseUrl + "api/v1/network/status";
        log.info("Requesting network status from: {} ", url);

        try{
            NetworkStatusDto response = restTemplate.getForObject(url, NetworkStatusDto.class);
            return Optional.ofNullable(response);
        }catch (RestClientException e){
            log.error("Error getting network status from bridge", e);
            return Optional.empty();
        }
    }


}
