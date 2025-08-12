package com.ovidius.adapter.embeds.Towny;

import com.ovidius.adapter.services.CrestService;
import com.ovidius.minecraft.client.dto.NationDto;
import com.ovidius.persistence.model.Crest;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.Instant;
import java.util.Optional;

@Component
public class NationEmbedFactory {

    private static final Color NATION_COLOR = new Color(255, 165, 0);
    private static final String DEFAULT_NATION_ICON="https://cdn-icons-png.flaticon.com/512/10946/10946937.png";

    private final CrestService crestService;

    public NationEmbedFactory(CrestService crestService) {
        this.crestService = crestService;
    }

    public MessageEmbed createMainNationEmbed(NationDto nation) {
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("⚜️ Нация " + nation.name())
                .setColor(NATION_COLOR);
        Optional<String> crestUrl = crestService.getCrestUrl(Crest.CrestTargetType.NATION, nation.name());
        builder.setThumbnail(crestUrl.orElse(DEFAULT_NATION_ICON));
        builder
                .addField("Король", "`" + nation.king() + "`", true)
                .addField("Столица", "`" + nation.capital() + "`", true)
                .addField("Банк", String.format(" `%,.2f`", nation.bank()), true)
                .addField("Города", String.format(" %d", nation.townCount()), true)
                .addField("Жители", String.format(" %d", nation.residentCount()), true)
                .addField("Союзники", nation.allies().isEmpty() ? "`Нет`" : "`" + String.join(", ", nation.allies()) + "`", false)
                .addField("Враги", nation.enemies().isEmpty() ? "`Нет`" : "`" + String.join(", ", nation.enemies()) + "`", false)
                .setTimestamp(Instant.now());
        return builder.build();
    }
    public MessageEmbed createNationNotFoundEmbed(String nationName) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Нация не найдена")
                .setDescription("Нация с названием `" + nationName + "` не существует.")
                .build();
    }
}
