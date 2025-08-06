package com.ovidius.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Rule {
    private String number;
    private String text;
    private String punishment;
    private String duration;
}
