package com.ovidius.botapp;

import com.ovidius.botapp.config.CommandRegistry;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;

@Component
public class BotInitializer {

    private final JDA jda;
    private final CommandRegistry commandRegistry;

    public BotInitializer(JDA jda, CommandRegistry commandRegistry) {
        this.jda = jda;
        this.commandRegistry = commandRegistry;
    }

    @PostConstruct
    public void init() {
        commandRegistry.registerCommands(jda);
    }
}
