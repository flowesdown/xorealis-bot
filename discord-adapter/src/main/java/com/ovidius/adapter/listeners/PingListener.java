package com.ovidius.adapter.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class PingListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            long time = System.currentTimeMillis();
            event.reply("Понг!").setEphemeral(true)
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("Понг: %d мс", System.currentTimeMillis() - time)
                    ).queue();
        }
    }
}