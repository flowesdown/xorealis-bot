package com.ovidius.persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GuidePage(
        String title,
        @JsonProperty("author_name") String authorName,
        @JsonProperty("author_icon_url") String authorIconUrl,
        String text
) {
}

