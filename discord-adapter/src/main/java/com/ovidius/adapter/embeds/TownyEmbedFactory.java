package com.ovidius.adapter.embeds;

import com.ovidius.minecraft.client.dto.NationDto;
import com.ovidius.minecraft.client.dto.TownDto;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class TownyEmbedFactory {
    private static final Color TOWN_COLOR = Color.CYAN;
    private static final Color NATION_COLOR = new Color(255, 165, 0);
    private static final String THUMBNAIL_URL = "https://png.pngtree.com/png-vector/20240621/ourmid/pngtree-house-and-minecraft-game-png-image_12711864.png";

    public enum View {
        MAIN((factory, town) -> factory.createMainTownEmbed(town)),
        RESIDENTS((factory, town) -> factory.createResidentsEmbed(town)),
        STATUS((factory, town) -> factory.createStatusEmbed(town));

        private final BiFunction<TownyEmbedFactory, TownDto, MessageEmbed> renderer;

        View(BiFunction<TownyEmbedFactory, TownDto, MessageEmbed> renderer) {
            this.renderer = renderer;
        }

        public MessageEmbed render(TownyEmbedFactory factory, TownDto town) {
            return renderer.apply(factory, town);
        }
    }

    public MessageEmbed createForView(View view, TownDto town) {
        return view.render(this, town);
    }


    public MessageEmbed createMainTownEmbed(TownDto town) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.addField("Нация", "`" + town.nation(), true);
        embed.addField("Мэр", town.mayor() + "`", true);
        embed.addField("Банк", String.format(" `%,.2f`", town.bank()), true);

        if (town.board() != null && !town.board().isEmpty()) {
            embed.addField("Доска объявлений", ">>> " + town.board(), false);
        }
        return embed.build();
    }
    public MessageEmbed createMainNationEmbed(NationDto nation) {
        return new EmbedBuilder()
                .setTitle("⚜️ Нация " + nation.name())
                .setColor(NATION_COLOR)
                .addField("Король", "`" + nation.king() + "`", true)
                .addField("Столица", "`" + nation.capital() + "`", true)
                .addField("Банк", String.format(" `%,.2f`", nation.bank()), true)
                .addField("Города", String.format(" %d", nation.townCount()), true)
                .addField("Жители", String.format(" %d", nation.residentCount()), true)
                .addField("Союзники", nation.allies().isEmpty() ? "`Нет`" : "`" + String.join(", ", nation.allies()) + "`", false)
                .addField("Враги", nation.enemies().isEmpty() ? "`Нет`" : "`" + String.join(", ", nation.enemies()) + "`", false)
                .setTimestamp(Instant.now())
                .build();
    }

    private MessageEmbed createResidentsEmbed(TownDto town) {
        EmbedBuilder builder = createBaseTownEmbed(town);
        String residentsList = String.join(", ", town.residents());
        builder.addField(
                String.format("👥 Жители (%d)", town.residentCount()),
                residentsList.isEmpty() ? "`В городе пока нет жителей.`" : ">>> " + residentsList,
                false
        );
        return builder.build();
    }

    private MessageEmbed createStatusEmbed(TownDto town) {
        EmbedBuilder builder = createBaseTownEmbed(town);
        StringBuilder statusText = new StringBuilder();
        statusText.append(town.isPublic() ? "✅ Открытый" : "❌ Закрытый").append("\n");
        statusText.append(town.isCapital() ? "👑 Столица нации" : "Провинциальный город");
        builder.addField("📜 Статусы", statusText.toString(), true);
        builder.addField("Налоги", "Ставка: `" + town.taxes() + "`", true);
        return builder.build();
    }

    public MessageEmbed createTownNotFoundEmbed(String townName) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Город не найден")
                .setDescription("Город с названием `"+townName+"` не существует на сервере Politic-Xдо ")
                .build();
    }
    public MessageEmbed createNationNotFoundEmbed(String nationName) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Нация не найдена")
                .setDescription("Нация с названием `" + nationName + "` не существует.")
                .build();
    }

    private EmbedBuilder createBaseTownEmbed(TownDto town) {
        return new EmbedBuilder()
                .setTitle("🏙️ Город " + town.name())
                .setThumbnail(THUMBNAIL_URL)
                .setColor(TOWN_COLOR)
                .setFooter("Интерактивное меню города")
                .setTimestamp(Instant.now());
    }
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
