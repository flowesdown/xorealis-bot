package com.ovidius.adapter.listeners;


import com.ovidius.adapter.services.ServerInfoService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerInfoListener extends ListenerAdapter {

    private final ServerInfoService serverInfoService;

    @Autowired
    public ServerInfoListener(ServerInfoService serverInfoService) {
        this.serverInfoService = serverInfoService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(!event.getName().equals("server")){
            return;
        }
        event.replyEmbeds(serverInfoService.createStatusEmbed()).queue();

    }


}
