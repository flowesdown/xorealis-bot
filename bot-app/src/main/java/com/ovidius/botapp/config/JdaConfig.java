package com.ovidius.botapp.config;


import com.ovidius.adapter.listeners.ServerInfoListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ovidius.adapter.listeners.PingListener;

@Configuration
public class JdaConfig {

    @Value("${discord.token}")
    private String token;

    @Bean
    public JDA jda(PingListener pingListener, ServerInfoListener serverInfoListener) throws InterruptedException {
        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("Сервер Xorealis"))
                .addEventListeners(pingListener, serverInfoListener)
                .build();

        jda.awaitReady();

        jda.upsertCommand("ping", "Проверить задержку бота").queue();
        jda.upsertCommand("server", "Показывает информацию о сервере").queue();

        return jda;
    }
}