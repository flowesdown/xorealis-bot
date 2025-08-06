package com.ovidius.adapter.embeds;

import com.ovidius.persistence.model.Rule;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import com.ovidius.persistence.model.RuleSet;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.Instant;

@Component
public class RulesEmbedFactory {
    private static final Color BRAND_COLOR_TURQUOISE = new Color(64, 224, 208);
    private static final String THUMBNAIL_URL = "https://media.discordapp.net/attachments/1401245388685447331/1402601053559586957/image.png?ex=68948186&is=6933006&hm=d51931aac1f948d6195f98ab6da74dc1759fa49c7db39b426dd4c00a73269a4b&=&format=webp&quality=lossless";

    public MessageEmbed createRuleEmbed(Rule rule, RuleSet ruleSet) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("📜 Правило " + rule.getNumber() + " | " + ruleSet.getTitle())
                .setColor(BRAND_COLOR_TURQUOISE)
                .setDescription(rule.getText())
                .setTimestamp(Instant.now());

        if (rule.getPunishment() != null && !rule.getPunishment().isEmpty()) {
            embed.addField("Наказание", rule.getPunishment(), true);
        }
        if (rule.getDuration() != null && !rule.getDuration().isEmpty()) {
            embed.addField("Длительность", rule.getDuration(), true);
        }

        return embed.build();
    }


    public MessageEmbed createRuleNotFoundEmbed(String type, String number) {
        return new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setTitle("🤔 Правило не найдено")
                .setDescription(String.format("Не удалось найти правило с номером **%s** в наборе правил **%s**.", number, type))
                .build();
    }


    public MessageEmbed createInvalidTypeEmbed(String type) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("🚫 Неверный тип правил")
                .setDescription(String.format("Набор правил **%s** не существует.\nДоступные наборы: `discord`, `politic-x`, `classic-x`", type))
                .build();
    }
}
