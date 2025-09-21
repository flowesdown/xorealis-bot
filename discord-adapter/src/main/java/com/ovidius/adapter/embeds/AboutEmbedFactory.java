package com.ovidius.adapter.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;
import java.awt.Color;
import java.time.Instant;

@Component
public class AboutEmbedFactory {


    private static final String BOT_AVATAR_URL = "https://easydonate.s3.easyx.ru/images/logos/58/e2/58e26009a0104812d144b733899dfb37816dcd0a7fea8e85b6eafb07b78dc2fc.png";
    private static final String AUTHOR_ICON_URL = "https://avatars.githubusercontent.com/u/191921847?s=400&u=738646439f130eb41f96057977b0a1c4bbe20db6&v=4";
    private static final String BOT_VERSION = "1.0.1";

    public MessageEmbed createAboutEmbed(User botUser, String authorDiscordTag) {

        return new EmbedBuilder()
                .setColor(new Color(0, 170, 170))
                .setThumbnail(BOT_AVATAR_URL)

                .setAuthor(botUser.getName(), null, botUser.getEffectiveAvatarUrl())
                .setTitle("Справочник по командам и информация")
                .setDescription("Я — кастомный бот, созданный специально для проекта **Xorealis**.\nМоя цель — предоставить удобный доступ к информации о серверах и добавить новые социальные возможности.")

                .addField("ℹ️ Информация",
                        "`/server` - Статус Minecraft-серверов\n" +
                                "`/rule` - Показать правило по номеру\n" +
                                "`/guide` - Интерактивный гайд по Towny\n" +
                                "`/about` - Это сообщение",
                        false)

                .addField("🏙️ Towny",
                        "`/town [название]` - Информация о городе\n" +
                                "`/nation [название]` - Информация о нации\n" +
                                "`/top [категория]` - Топы городов и наций",
                        true)

                .addField("💍 Браки",
                        "`/propose [игрок]` - Сделать предложение\n" +
                                "`/divorce` - Расторгнуть брак\n" +
                                "`/couple [игрок]` - Профиль пары",
                        true)

                .addField("💕 Взаимодействия",
                        "`/kiss`, `/hug` (*и синонимы*)\n" +
                                "`/couple-top` - Топ пар",
                        true)

                .addField("👑 Разработчик", "SnappyWave (`" + authorDiscordTag + "`)", true)

                .setFooter("Версия: " + BOT_VERSION, AUTHOR_ICON_URL)
                .setTimestamp(Instant.now())
                .build();
    }
}

