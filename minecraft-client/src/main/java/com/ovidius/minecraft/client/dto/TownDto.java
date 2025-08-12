package com.ovidius.minecraft.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record TownDto (
        String name,
        String nation,
        String mayor,
        int residentCount,
        double bank,
        double taxes,
        String board,
        List<String> residents,
        @JsonProperty("capital")
        boolean isCapital,

        @JsonProperty("public")
        boolean isPublic,

        @JsonProperty("neutral")
        boolean isNeutral
){}
