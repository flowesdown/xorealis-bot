package com.ovidius.persistence.repository;

import com.ovidius.persistence.model.RuleSet;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RulesRepository {
    private final Map<String, RuleSet> storage = new ConcurrentHashMap<>();

    public void save(String type, RuleSet ruleSet){
        storage.put(type, ruleSet);
    }
    public Optional<RuleSet> findByType(String type){
        return Optional.ofNullable(storage.get(type));
    }
}
