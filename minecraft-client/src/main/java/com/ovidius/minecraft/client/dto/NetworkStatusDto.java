package com.ovidius.minecraft.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class NetworkStatusDto {

    @JsonProperty("total_online")
    private int totalOnline;

    @JsonProperty("servers")
    private Map<String, Integer> servers;
}
