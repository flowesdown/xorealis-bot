package com.ovidius.adapter.listeners;

import com.ovidius.adapter.embeds.Towny.NationEmbedFactory;
import com.ovidius.adapter.embeds.Towny.TopListEmbedFactory;
import com.ovidius.adapter.embeds.Towny.TownEmbedFactory;
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
    private final TownEmbedFactory townEmbedFactory;
    private final NationEmbedFactory nationEmbedFactory;
    private final TopListEmbedFactory townListEmbedFactory;
    private final TopListEmbedFactory topListEmbedFactory;

    public TownyListener(XorealisBridgeClient client,
                         TownEmbedFactory townEmbedFactory,
                         NationEmbedFactory nationEmbedFactory,
                         TopListEmbedFactory townListEmbedFactory, TopListEmbedFactory topListEmbedFactory) {
        this.client = client;
        this.townEmbedFactory = townEmbedFactory;
        this.nationEmbedFactory = nationEmbedFactory;
        this.townListEmbedFactory = townListEmbedFactory;
        this.topListEmbedFactory = topListEmbedFactory;
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
                        event.replyEmbeds(townEmbedFactory.createForView(TownEmbedFactory.View.MAIN, town))
                                .addActionRow(
                                        createTownButton(TownEmbedFactory.View.MAIN, townName, true),
                                        createTownButton(TownEmbedFactory.View.RESIDENTS, townName, false),
                                        createTownButton(TownEmbedFactory.View.STATUS, townName, false)
                                ).queue()
                , () ->
                        event.replyEmbeds(townEmbedFactory.createTownNotFoundEmbed(townName)).setEphemeral(true).queue()
        );
    }

    private void handleNation(SlashCommandInteractionEvent event) {
        String nationName = event.getOption("name").getAsString();
        client.getNationByName(nationName).ifPresentOrElse(nation ->
                        event.replyEmbeds(nationEmbedFactory.createMainNationEmbed(nation)).queue()
                , () ->
                        event.replyEmbeds(nationEmbedFactory.createNationNotFoundEmbed(nationName)).setEphemeral(true).queue()
        );
    }

    private void handleTop(SlashCommandInteractionEvent event) {
        String subcommand = event.getSubcommandName();
        MessageEmbed embed = null;

        switch (subcommand) {
            case "towns-balance" -> {
                List<TownDto> towns = client.getTopTownsByBalance();
                embed = topListEmbedFactory.createTopTownsEmbed("Топ-10 городов по балансу", towns, "💰", TownDto::bank);
            }
            case "towns-residents" -> {
                List<TownDto> towns = client.getTopTownsByResidents();
                embed = topListEmbedFactory.createTopTownsEmbed("Топ-10 городов по жителям", towns, "👥", TownDto::residentCount);
            }
            case "nations-balance" -> {
                List<NationDto> nations = client.getTopNationsByBalance();
                embed = topListEmbedFactory.createTopNationsEmbed("Топ-10 наций по балансу", nations, "💰", NationDto::bank);
            }
            case "nations-residents" -> {
                List<NationDto> nations = client.getTopNationsByResidents();
                embed = topListEmbedFactory.createTopNationsEmbed("Топ-10 наций по жителям", nations, "👥", NationDto::residentCount);
            }
            case "nations-landsize" -> {
                List<NationDto> nations = client.getTopNationsByLandSize();
                embed = topListEmbedFactory.createTopNationsEmbed("Топ-10 наций по размеру", nations, "🏙️", NationDto::townCount);
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
        TownEmbedFactory.View view = TownEmbedFactory.View.valueOf(parts[1]);
        String townName = parts[2];

        client.getTownByName(townName).ifPresent(town -> {
            event.editMessageEmbeds(townEmbedFactory.createForView(view, town))
                    .setActionRow(
                            createTownButton(TownEmbedFactory.View.MAIN, townName, view == TownEmbedFactory.View.MAIN),
                            createTownButton(TownEmbedFactory.View.RESIDENTS, townName, view == TownEmbedFactory.View.RESIDENTS),
                            createTownButton(TownEmbedFactory.View.STATUS, townName, view == TownEmbedFactory.View.STATUS)
                    ).queue();
        });
    }

    private Button createTownButton(TownEmbedFactory.View view, String townName, boolean isPrimary) {
        String label = switch (view) {
            case MAIN -> "\uD83C\uDFE0 Главная";
            case RESIDENTS -> "\uD83D\uDC65 Жители";
            case STATUS -> "\uD83D\uDCDC Статус";
        };
        String id = "town-view:" + view.name() + ":" + townName;

        return isPrimary ? Button.primary(id, label) : Button.secondary(id, label);
    }

}
