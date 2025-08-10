package com.ovidius.adapter.listeners;

import com.ovidius.adapter.embeds.TownyEmbedFactory;
import com.ovidius.minecraft.client.dto.NationDto;
import com.ovidius.minecraft.client.dto.TownDto;
import com.ovidius.minecraft.client.services.XorealisBridgeClient;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;


import java.util.List;


@Component
public class TownyListener extends ListenerAdapter {
    private final XorealisBridgeClient client;
    private TownyEmbedFactory embedFactory;

    public TownyListener(XorealisBridgeClient client, TownyEmbedFactory embedFactory) {
        this.client = client;
        this.embedFactory = embedFactory;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "town" -> handleTown(event);
            case "nation" -> handleNation(event);
            case "top" -> handleTop(event);
        }
    }

    private void handleTown(SlashCommandInteractionEvent event) {
        String townName = event.getOption("name").getAsString();

        client.getTownByName(townName).ifPresentOrElse(town ->
                        event.replyEmbeds(embedFactory.createForView(TownyEmbedFactory.View.MAIN, town))
                                .addActionRow(
                                        createTownButton(TownyEmbedFactory.View.MAIN, townName, true),
                                        createTownButton(TownyEmbedFactory.View.RESIDENTS, townName, false),
                                        createTownButton(TownyEmbedFactory.View.STATUS, townName, false)
                                ).queue()
                , () ->
                        event.replyEmbeds(embedFactory.createTownNotFoundEmbed(townName)).setEphemeral(true).queue()
        );
    }

    private void handleNation(SlashCommandInteractionEvent event) {
        String nationName = event.getOption("name").getAsString();
        client.getNationByName(nationName).ifPresentOrElse(nation ->
                        event.replyEmbeds(embedFactory.createMainNationEmbed(nation)).queue()
                , () ->
                        event.replyEmbeds(embedFactory.createNationNotFoundEmbed(nationName)).setEphemeral(true).queue()
        );
    }

    private void handleTop(SlashCommandInteractionEvent event) {
        String subcommand = event.getSubcommandName();
        MessageEmbed embed = null;

        switch (subcommand) {
            case "towns-balance" -> {
                List<TownDto> towns = client.getTopTownsByBalance();
                embed = embedFactory.createTopTownsEmbed("Топ-10 городов по балансу", towns, "💰", TownDto::bank);
            }
            case "towns-residents" -> {
                List<TownDto> towns = client.getTopTownsByResidents();
                embed = embedFactory.createTopTownsEmbed("Топ-10 городов по жителям", towns, "👥", TownDto::residentCount);
            }
            case "nations-balance" -> {
                List<NationDto> nations = client.getTopNationsByBalance();
                embed = embedFactory.createTopNationsEmbed("Топ-10 наций по балансу", nations, "💰", NationDto::bank);
            }
            case "nations-residents" -> {
                List<NationDto> nations = client.getTopNationsByResidents();
                embed = embedFactory.createTopNationsEmbed("Топ-10 наций по жителям", nations, "👥", NationDto::residentCount);
            }
            case "nations-landsize" -> {
                List<NationDto> nations = client.getTopNationsByLandSize();
                embed = embedFactory.createTopNationsEmbed("Топ-10 наций по размеру", nations, "🏙️", NationDto::townCount);
            }
        }

        if (embed != null) {
            event.replyEmbeds(embed).queue();
        } else {
            event.reply("❌ Неизвестная подкоманда топа. Разработчик что-то сломал...").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();
        if (!componentId.startsWith("town-view:")) return;

        String[] parts = componentId.split(":", 3);
        TownyEmbedFactory.View view = TownyEmbedFactory.View.valueOf(parts[1]);
        String townName = parts[2];

        client.getTownByName(townName).ifPresent(town -> {
            event.editMessageEmbeds(embedFactory.createForView(view, town))
                    .setActionRow(
                            createTownButton(TownyEmbedFactory.View.MAIN, townName, view == TownyEmbedFactory.View.MAIN),
                            createTownButton(TownyEmbedFactory.View.RESIDENTS, townName, view == TownyEmbedFactory.View.RESIDENTS),
                            createTownButton(TownyEmbedFactory.View.STATUS, townName, view == TownyEmbedFactory.View.STATUS)
                    ).queue();
        });
    }

    private Button createTownButton(TownyEmbedFactory.View view, String townName, boolean isPrimary) {
        String label = switch (view) {
            case MAIN -> "\uD83C\uDFE0 Главная";
            case RESIDENTS -> "\uD83D\uDC65 Жители";
            case STATUS -> "\uD83D\uDCDC Статус";
        };
        String id = "town-view:" + view.name() + ":" + townName;

        return isPrimary ? Button.primary(id, label) : Button.secondary(id, label);
    }

}
