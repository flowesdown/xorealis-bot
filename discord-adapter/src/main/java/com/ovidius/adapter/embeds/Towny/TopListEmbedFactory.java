package com.ovidius.adapter.embeds.Towny;

import com.ovidius.minecraft.client.dto.NationDto;
import com.ovidius.minecraft.client.dto.TownDto;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;

@Component
public class TopListEmbedFactory {

    public MessageEmbed createTopTownsEmbed(String title, List<TownDto> towns, String valueLabel, Function<TownDto, Number> valueExtractor) {
        EmbedBuilder builder = new EmbedBuilder().setTitle("🏆 " + title).setColor(Color.YELLOW);

        if (towns == null || towns.isEmpty()) {
            builder.setDescription("Пока нет данных для отображения топа.");
            return builder.build();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < towns.size(); i++) {
            TownDto town = towns.get(i);
            String medal = getMedal(i + 1);
            Number value = valueExtractor.apply(town);

            String formattedValue = (value instanceof Double)
                    ? String.format("%,.2f", value.doubleValue())
                    : String.format("%,d", value.longValue());

            sb.append(String.format("%s **%s** — `%s` %s\n", medal, town.name(), formattedValue, valueLabel));
        }
        builder.setDescription(sb.toString());
        builder.setTimestamp(Instant.now());
        return builder.build();
    }

    public MessageEmbed createTopNationsEmbed(String title, List<NationDto> nations, String valueLabel, Function<NationDto, Number> valueExtractor) {
        EmbedBuilder builder = new EmbedBuilder().setTitle("🏆 " + title).setColor(Color.ORANGE);

        if (nations == null || nations.isEmpty()) {
            builder.setDescription("Пока нет данных для отображения топа.");
            return builder.build();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nations.size(); i++) {
            NationDto nation = nations.get(i);
            String medal = getMedal(i + 1);
            Number value = valueExtractor.apply(nation);

            String formattedValue = (value instanceof Double)
                    ? String.format("%,.2f", value.doubleValue())
                    : String.format("%,d", value.longValue());

            sb.append(String.format("%s **%s** — `%s` %s\n", medal, nation.name(), formattedValue, valueLabel));
        }
        builder.setDescription(sb.toString());
        builder.setTimestamp(Instant.now());
        return builder.build();
    }


    private String getMedal(int rank) {
        return switch (rank) {
            case 1 -> "🥇";
            case 2 -> "🥈";
            case 3 -> "🥉";
            default -> String.format("`%d.`", rank);
        };
    }

}
