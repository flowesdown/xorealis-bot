package com.ovidius.adapter.listeners;

import com.ovidius.adapter.services.RulesService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RulesListener extends ListenerAdapter {

    private final RulesService rulesService;
    private static final Map<String, String> TYPE_ALIASES = Map.of(
            "d", "discord",
            "discord", "discord", // Добавляем и полные имена на всякий случай
            "c", "classic-x",
            "classic", "classic-x",
            "classic-x", "classic-x",
            "p", "politic-x",
            "politic", "politic-x",
            "politic-x", "politic-x"
    );

    public RulesListener(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("rule")) {
            return;
        }

        String userInputType = event.getOption("type", OptionMapping::getAsString);
        String number = event.getOption("number", OptionMapping::getAsString);

        String technicalType = TYPE_ALIASES.getOrDefault(userInputType.toLowerCase(), userInputType);

        event.replyEmbeds(rulesService.findAndCreateRuleEmbed(technicalType, number)).queue();
    }
}