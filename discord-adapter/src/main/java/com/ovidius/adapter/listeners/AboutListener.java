package com.ovidius.adapter.listeners;

import com.ovidius.adapter.embeds.AboutEmbedFactory;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AboutListener extends ListenerAdapter {

    private final AboutEmbedFactory embedFactory;



    @Value("${discord.author.tag}")
    private String authorDiscordTag;

    public AboutListener(AboutEmbedFactory embedFactory) {
        this.embedFactory = embedFactory;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("about")) return;

        event.replyEmbeds(embedFactory.createAboutEmbed(event.getJDA().getSelfUser(), authorDiscordTag)).queue();
    }
}
