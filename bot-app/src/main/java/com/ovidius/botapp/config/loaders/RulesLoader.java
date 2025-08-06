package com.ovidius.botapp.config.loaders;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ovidius.persistence.model.RuleSet;
import com.ovidius.persistence.repository.RulesRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RulesLoader {

    private static final Logger log = LoggerFactory.getLogger(RulesLoader.class);
    private final RulesRepository repository;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public RulesLoader(RulesRepository repository) {
        this.repository = repository;
    }
    @PostConstruct
    public void loadRules() throws IOException {
        log.info("Loading rules...");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:rules/*.yml");

        for(Resource resource : resources) {
            String type = resource.getFilename().replace(".yml", "");
            try{
                RuleSet ruleSet = mapper.readValue(resource.getInputStream(), RuleSet.class);
                repository.save(type, ruleSet);
                log.info("Successfully loaded rules for type '{}' from {}", type, resource.getFilename());
            }catch (IOException e){
                log.error("Failed to load rules for type '{}' from {}", type, resource.getFilename()+" "+e);
            }
        }
    }
}
