package com.ovidius.minecraft.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record TownDto (
        String name,
        String nation,
        String mayor,
        int residentCount,
        double bank,
        double taxes,
        boolean isPublic,
        boolean isCapital,
        String board,
        List<String> residents
){}
