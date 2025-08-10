package com.ovidius.minecraft.client.services;

import com.ovidius.minecraft.client.dto.NationDto;
import com.ovidius.minecraft.client.dto.NetworkStatusDto;
import com.ovidius.minecraft.client.dto.TownDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class XorealisBridgeClient {

    private static final Logger log = LoggerFactory.getLogger(XorealisBridgeClient.class);

    private static final String API_V1 = "/api/v1";

    private static final String NETWORK_STATUS_ENDPOINT = API_V1 + "/network/status";

    private static final String TOWNS_ENDPOINT = API_V1 + "/towns";
    private static final String TOWN_BY_NAME_ENDPOINT = TOWNS_ENDPOINT + "/{name}";
    private static final String TOWNS_TOP_BALANCE_ENDPOINT = TOWNS_ENDPOINT + "/top/balance";
    private static final String TOWNS_TOP_RESIDENTS_ENDPOINT = TOWNS_ENDPOINT + "/top/residents";
    private static final String TOWNS_TOP_TAX_RATE_ENDPOINT = TOWNS_ENDPOINT + "/top/tax-rate";

    private static final String NATIONS_ENDPOINT = API_V1 + "/nations";
    private static final String NATION_BY_NAME_ENDPOINT = NATIONS_ENDPOINT + "/{name}";
    private static final String NATIONS_TOP_BALANCE_ENDPOINT = NATIONS_ENDPOINT + "/top/balance";
    private static final String NATIONS_TOP_RESIDENTS_ENDPOINT = NATIONS_ENDPOINT + "/top/residents";
    private static final String NATIONS_TOP_LANDSIZE_ENDPOINT = NATIONS_ENDPOINT + "/top/landsize";

    private final RestTemplate restTemplate;

    @Value("${xorealis.bridge.api.network-url}")
    private String networkApiUrl;

    @Value("${xorealis.bridge.api.towny-url}")
    private String townyApiUrl;

    public XorealisBridgeClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Optional<NetworkStatusDto> getNetworkStatus() {
        return getObject(networkApiUrl + NETWORK_STATUS_ENDPOINT, NetworkStatusDto.class, "Network Status");
    }


}
