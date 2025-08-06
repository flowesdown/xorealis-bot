package com.ovidius.adapter.services;

import com.ovidius.adapter.embeds.RulesEmbedFactory;
import com.ovidius.persistence.model.Rule;
import com.ovidius.persistence.repository.RulesRepository;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RulesService {

    private final RulesRepository rulesRepository;
    private final RulesEmbedFactory embedFactory;

    public RulesService(RulesRepository rulesRepository, RulesEmbedFactory embedFactory) {
        this.rulesRepository = rulesRepository;
        this.embedFactory = embedFactory;
    }

    public MessageEmbed findAndCreateRuleEmbed(String type, String number) {
        return rulesRepository.findByType(type)
                .map(ruleSet -> {
                    Optional<Rule> foundRule = ruleSet.getRules().stream()
                            .filter(rule -> rule.getNumber().equals(number))
                            .findFirst();
                    return foundRule
                            .map(rule -> embedFactory.createRuleEmbed(rule, ruleSet))
                            .orElseGet(() -> embedFactory.createRuleNotFoundEmbed(type, number));
                })
                .orElseGet(() -> embedFactory.createInvalidTypeEmbed(type));
    }
}