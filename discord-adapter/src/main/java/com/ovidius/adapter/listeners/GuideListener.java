package com.ovidius.adapter.listeners;

import com.ovidius.adapter.embeds.GuideEmbedFactory;
import com.ovidius.persistence.repository.GuideRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GuideListener extends ListenerAdapter {
    private final GuideRepository guideRepository;
    private final GuideEmbedFactory embedFactory;

    public GuideListener(GuideRepository repository, GuideEmbedFactory factory) {
        this.guideRepository = repository;
        this.embedFactory = factory;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("guide")) return;
        String guideName = event.getOption("name").getAsString().toLowerCase();

        guideRepository.findByName(guideName).ifPresentOrElse(guide -> {
            event.replyEmbeds(embedFactory.createPageEmbed(guide, 0))
                    .addActionRow(createNavButtons(guideName, 0, guide.pages().size()))
                    .queue();
        }, () ->
                event.replyEmbeds(embedFactory.createGuideNotFoundEmbed(guideName)).setEphemeral(true).queue());
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();
        if (!componentId.startsWith("guide-nav:")) return;

        String[] parts = componentId.split(":", 4);
        String guideName = parts[1];
        String action = parts[2];
        int currentPage = Integer.parseInt(parts[3]);

        guideRepository.findByName(guideName).ifPresent(guide -> {
            int totalPages = guide.pages().size();
            int newPage = switch (action) {
                case "first" -> 0;
                case "prev"  -> Math.max(0, currentPage - 1);
                case "next"  -> Math.min(totalPages - 1, currentPage + 1);
                case "last"  -> totalPages - 1;
                default -> currentPage;
            };

            if (newPage == currentPage) {
                event.deferEdit().queue();
                return;
            }

            event.editMessageEmbeds(embedFactory.createPageEmbed(guide, newPage))
                    .setActionRow(createNavButtons(guideName, newPage, totalPages))
                    .queue();
        });
    }

    private List<Button> createNavButtons(String guideName, int currentPage, int totalPages) {
        return List.of(
                Button.primary("guide-nav:" + guideName + ":first:" + currentPage, "⏪").withDisabled(currentPage == 0),
                Button.secondary("guide-nav:" + guideName + ":prev:" + currentPage, "◀️").withDisabled(currentPage == 0),
                Button.secondary("guide-nav:" + guideName + ":next:" + currentPage, "▶️").withDisabled(currentPage >= totalPages - 1),
                Button.primary("guide-nav:" + guideName + ":last:" + currentPage, "⏩").withDisabled(currentPage >= totalPages - 1)
        );
    }
}
