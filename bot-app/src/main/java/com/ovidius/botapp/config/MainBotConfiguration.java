package com.ovidius.botapp.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@Configuration
@ComponentScan(basePackages = {
        "com.ovidius.adapter",
        "com.ovidius.minecraft.client",
        "com.ovidius.persistence",
        "com.ovidius.botapp"
})
@EntityScan(basePackages = "com.ovidius.persistence.model")
@EnableJpaRepositories(basePackages = "com.ovidius.persistence.repository")
public class MainBotConfiguration {
}