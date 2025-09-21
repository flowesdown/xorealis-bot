package com.ovidius.minecraft.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NetworkStatusDto(
        @JsonProperty("total_online") int totalOnline,
        Map<String, Integer> servers
) {


}
