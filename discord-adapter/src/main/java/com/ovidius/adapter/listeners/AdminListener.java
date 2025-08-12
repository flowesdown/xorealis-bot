package com.ovidius.adapter.listeners;

import com.ovidius.adapter.services.CrestService;
import com.ovidius.persistence.model.Crest;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class AdminListener extends ListenerAdapter {

    private final CrestService crestService;

    public AdminListener(CrestService crestService) {
        this.crestService = crestService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(!event.getName().equals("set-crest")) return;

        if(!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.reply("У вас нет прав на использование этой комманды!").setEphemeral(true).queue();
            return;
        }

        event.deferReply(true).queue();

        String typeStr = event.getOption("type").getAsString();
        String name = event.getOption("name").getAsString();
        String url = event.getOption("url").getAsString();

        try {
            Crest.CrestTargetType type = Crest.CrestTargetType.valueOf(typeStr.toUpperCase());
            crestService.setCrest(type, name, url);
            event.getHook().sendMessage("Герб для `"+typeStr+"` **"+name+"** успешно установлен!").setEphemeral(true).queue();
        }catch(IllegalArgumentException e) {
            event.getHook().sendMessage("Неверный тип. Укажите `TOWN` или `Nation`.").setEphemeral(true).queue();
        }
    }


}
