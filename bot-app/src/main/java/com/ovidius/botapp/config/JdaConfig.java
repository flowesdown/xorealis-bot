package com.ovidius.botapp.config;


import com.ovidius.adapter.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdaConfig {

    @Value("${discord.token}")
    private String token;

    private final CommandRegistry commandRegistry;

    public JdaConfig(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Bean
    public JDA jda(PingListener pingListener, ServerInfoListener serverInfoListener,
                   RulesListener rulesListener, MarriageListener marriageListener, TownyListener townListener) throws InterruptedException {

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("Сервер Xorealis"))
                .addEventListeners(pingListener, serverInfoListener, rulesListener, marriageListener,townListener)
                .build();

        // Теперь этот вызов легален
        jda.awaitReady();

        commandRegistry.registerCommands(jda);

        return jda;
    }
}