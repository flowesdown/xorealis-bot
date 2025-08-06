package com.ovidius.botapp.config;


import com.ovidius.adapter.listeners.RulesListener;
import com.ovidius.adapter.listeners.ServerInfoListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
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
    public JDA jda(PingListener pingListener, ServerInfoListener serverInfoListener, RulesListener rulesListener) throws InterruptedException {
        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("Сервер Xorealis"))
                .addEventListeners(pingListener, serverInfoListener, rulesListener)
                .build();

        jda.awaitReady();

        jda.updateCommands().addCommands(
                Commands.slash("ping", "Проверить задержку бота"),
                Commands.slash("server", "Показывает информацию о сервере"),
                Commands.slash("rule", "Показывает правило по его номеру")
                        .addOption(OptionType.STRING, "type", "Тип правил (d (discord), p (politic-x), c (classic-x))", true)
                        .addOption(OptionType.STRING, "number", "Номер правила (например, 1.1)", true)
        ).queue();

        return jda;
    }
}