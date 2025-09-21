package com.ovidius.adapter.embeds;

import com.ovidius.minecraft.client.dto.NetworkStatusDto;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatusEmbedFactory {

    private static final String THUMBNAIL_URL = "https://media.discordapp.net/attachments/1401245388685447331/1402601053559586957/image.png?ex=68948186&is=68933006&hm=d51931aac1f948d6195f98ab6da74dc1759fa49c7db39b426dd4c00a73269a4b&=&format=webp&quality=lossless";
    private static final String TITLE = "Статус серверов Xorealis";

    private static final Map<String, String> SERVER_NAME_MAPPINGS = Map.of(
            "politic-x", "Политика",
            "classic-x", "Классика",
            "lobby", "Лобби",
            "duels-x", "Дуэли"
    );

    //    public MessageEmbed createSuccessEmbed(NetworkStatusDto status){
//        String totalOnlineText = "👥 " + status.totalOnline();
//        if (status.totalOnline() == 0) {
//            totalOnlineText = "😴 " + status.totalOnline();
//        }
//
//        return new EmbedBuilder()
//                .setTitle(TITLE)
//                .setColor(status.totalOnline() > 0 ? Color.GREEN : Color.ORANGE)
//                .setThumbnail(THUMBNAIL_URL)
//                .addField("Общий онлайн", totalOnlineText, false)
//                .addField("Онлайн по серверам", formatServersInfo(status), false) // <-- ИСПОЛЬЗУЕМ НАШ МЕТОД-ПОМОЩНИК
//                .setFooter("Данные получены в реальном времени.")
//                .setTimestamp(Instant.now())
//                .build();
//    }
    public MessageEmbed createSuccessEmbed(NetworkStatusDto status) {
        String totalOnlineText = "👥 " + status.totalOnline();
        if (status.totalOnline() == 0) {
            totalOnlineText = "😴 " + status.totalOnline();
        }

        return new EmbedBuilder()
                .setTitle(TITLE)
                .setColor(Color.CYAN)
                .setThumbnail(THUMBNAIL_URL)
                .addField("Общий онлайн", totalOnlineText, false)
                .addField("Онлайн по серверам", formatServersInfo(status), false)
                .setFooter("Данные получены в реальном времени.")
                .setTimestamp(Instant.now())
                .build();
    }

    public MessageEmbed createErrorEmbed() {
        return new EmbedBuilder()
                .setTitle(TITLE)
                .setColor(Color.RED)
                .setThumbnail(THUMBNAIL_URL)
                .setDescription("❌ **Сервер недоступен**\nНе удалось получить информацию о статусе серверов. Попробуйте снова через несколько минут.")
                .setFooter("Возможно, сервер перезагружается или проводятся технические работы")
                .setTimestamp(Instant.now())
                .build();
    }


    //    private String formatServersInfo(NetworkStatusDto status) {
//        if (status.servers() == null || status.servers().isEmpty()) {
//            return "`Нет данных по отдельным серверам.`";
//        }
//
//        return status.servers().entrySet().stream()
//                .map(entry -> {
//                    String prettyName = SERVER_NAME_MAPPINGS.getOrDefault(entry.getKey(), entry.getKey());
//                    return String.format("`%s`: **%d** игроков", prettyName, entry.getValue());
//                })
//                .collect(Collectors.joining("\n"));
//    }
    private String formatServersInfo(NetworkStatusDto status) {
        if (status.servers() == null || status.servers().isEmpty()) {
            return "`Нет данных по отдельным серверам.`";
        }

        return status.servers().entrySet().stream()
                .map(entry -> {
                    String prettyName = SERVER_NAME_MAPPINGS.getOrDefault(entry.getKey(), entry.getKey());
                    return String.format("`%s`:  Вайпается...", prettyName);
                })
                .collect(Collectors.joining("\n"));
    }
}