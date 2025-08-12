package com.ovidius.adapter.embeds.Towny;

import com.ovidius.adapter.services.CrestService;
import com.ovidius.minecraft.client.dto.TownDto;
import com.ovidius.persistence.model.Crest;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.Instant;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
public class TownEmbedFactory {
    private final CrestService crestService;

    public TownEmbedFactory(CrestService crestService) {
        this.crestService = crestService;
    }

    private static final Color TOWN_COLOR = Color.CYAN;
    private static final String DEFAULT_TOWN_ICON = "https://png.pngtree.com/png-vector/20240621/ourmid/pngtree-house-and-minecraft-game-png-image_12711864.png";

    public enum View {
        MAIN((factory, town) -> factory.createMainTownEmbed(town)),
        RESIDENTS((factory, town) -> factory.createResidentsEmbed(town)),
        STATUS((factory, town) -> factory.createStatusEmbed(town));

        private final BiFunction<TownEmbedFactory, TownDto, MessageEmbed> renderer;

        View(BiFunction<TownEmbedFactory, TownDto, MessageEmbed> renderer) {
            this.renderer = renderer;
        }

        public MessageEmbed render(TownEmbedFactory factory, TownDto town) {
            return renderer.apply(factory, town);
        }
    }

    public MessageEmbed createForView(View view, TownDto town) {
        return view.render(this, town);
    }


    private MessageEmbed createMainTownEmbed(TownDto town) {
        EmbedBuilder builder = createBaseTownEmbed(town);

        Optional<String> crestUrl = crestService.getCrestUrl(Crest.CrestTargetType.TOWN, town.name());
        builder.setThumbnail(crestUrl.orElse(DEFAULT_TOWN_ICON));

        builder.addField("Нация", "`" + town.nation() + "`", true);
        builder.addField("Мэр", "`" + town.mayor() + "`", true);
        builder.addField("Банк", String.format("💰 `%,.2f`", town.bank()), true);

        if (town.board() != null && !town.board().isBlank()) {
            builder.addField("Доска объявлений", ">>> " + town.board(), false);
        }
        return builder.build();
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
        Optional<String> crestUrl = crestService.getCrestUrl(Crest.CrestTargetType.TOWN, town.name());
        builder.setThumbnail(crestUrl.orElse(DEFAULT_TOWN_ICON));
        StringBuilder statusText = new StringBuilder();
        statusText.append(town.isPublic() ? "✅ Открытый" : "❌ Закрытый").append("\n");
        statusText.append(town.isCapital() ? "👑 Столица нации" : "Провинциальный город").append("\n");
        statusText.append(town.isNeutral() ? "🕊️ Нейтральный" : "⚔️ Воюющий");
        builder.addField("📜 Статусы", statusText.toString(), true);
        builder.addField("Налоги", "Ставка: `" + town.taxes() + "`", true);
        return builder.build();
    }

    public MessageEmbed createTownNotFoundEmbed(String townName) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Город не найден")
                .setDescription("Город с названием `" + townName + "` не существует на сервере Politic-Xдо ")
                .build();
    }

    private EmbedBuilder createBaseTownEmbed(TownDto town) {
        return new EmbedBuilder()
                .setTitle("🏙️ Город " + town.name())
                .setColor(TOWN_COLOR)
                .setFooter("Интерактивное меню города")
                .setTimestamp(Instant.now());
    }

}
