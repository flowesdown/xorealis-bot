package com.ovidius.minecraft.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NationDto (

    String name,
    String king,
    String capital,
    int townCount,
    int residentCount,
    double bank,
    List<String> allies,
    List<String> enemies,
    List<String> towns

){}
