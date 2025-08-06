package com.ovidius.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RuleSet {
    private String title;
    private String intro;
    private List<Rule> rules;
}
