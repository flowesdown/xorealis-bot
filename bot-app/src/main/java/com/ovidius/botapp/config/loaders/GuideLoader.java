package com.ovidius.botapp.config.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ovidius.persistence.model.Guide;
import com.ovidius.persistence.repository.GuideRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GuideLoader {
    private static final Logger log= LoggerFactory.getLogger(GuideLoader.class);
    private final GuideRepository repository;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public GuideLoader(GuideRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void loadGuides() {
        log.info("Loading Guides...");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try{
            Resource[] resources = resolver.getResources("classpath:guides/*.yml");
            for (Resource resource : resources) {
                String guideName=resource.getFilename().replace(".yml","");
                try {
                    Guide guide = mapper.readValue(resource.getInputStream(),Guide.class);
                    repository.save(guideName,guide);
                    log.info("Successfully loaded guide {} from {}",guideName,resource.getFilename());
                }catch (IOException e){
                    log.error("Failed to load guide from {}", resource.getFilename(), e);
                }
            }
        }catch (IOException e){
            log.error("Could not find any guides in guides/ directory.", e);        }
    }
}
