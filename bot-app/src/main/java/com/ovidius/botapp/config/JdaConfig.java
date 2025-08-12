package com.ovidius.botapp.config;


import com.ovidius.adapter.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JdaConfig {

    @Value("${discord.token}")
    private String token;

    @Bean
    public JDA jda(List<ListenerAdapter> listeners) throws InterruptedException {

        JDABuilder builder = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("Сервер Xorealis"));

        listeners.forEach(builder::addEventListeners);

        return builder.build().awaitReady();
    }
}