package com.ovidius.adapter.embeds;

import com.ovidius.persistence.model.Guide;
import com.ovidius.persistence.model.GuidePage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;
import java.awt.Color;

@Component
public class GuideEmbedFactory {

    public MessageEmbed createPageEmbed(Guide guide, int pageIndex) {
        GuidePage page = guide.pages().get(pageIndex);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode(guide.color()));
        builder.setDescription(page.text());
        String authorName = page.authorName();
        String authorIconUrl = page.authorIconUrl();

        if (authorName != null && !authorName.isBlank() &&
                authorIconUrl != null && authorIconUrl.startsWith("http")) {
            builder.setAuthor(authorName, null, authorIconUrl);
            builder.setTitle(page.title());
        } else {
            builder.setTitle(String.format("%s | %s", guide.title(), page.title()));
        }
        builder.setFooter(String.format("Страница %d из %d", pageIndex + 1, guide.pages().size()));

        return builder.build();
    }

    public MessageEmbed createGuideNotFoundEmbed(String guideName) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("🚫 Гайд не найден")
                .setDescription("Гайд с названием `" + guideName + "` не существует.")
                .build();
    }
}
