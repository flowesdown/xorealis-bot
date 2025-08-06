package com.ovidius.adapter.embeds;

import com.ovidius.minecraft.client.dto.NetworkStatusDto;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatusEmbedFactory {

    private final String THUMBNAIL_URL = "https://media.discordapp.net/attachments/1401245388685447331/1402601053559586957/image.png?ex=68948186&is=68933006&hm=d51931aac1f948d6195f98ab6da74dc1759fa49c7db39b426dd4c00a73269a4b&=&format=webp&quality=lossless";

    private static final String TITLE = "Статус серверов Xorealis";

    private static final Map<String,String> SERVER_NAME_MAPPINGS = Map.of(
            "politic-x", "Политика",
            "classic-x", "Классика",
            "lobby", "Лобби"
            );

    public MessageEmbed createSuccessEmbed(NetworkStatusDto status){
        String serversInfo = formatServersInfo(status);
        String totalOnline ="\uD83D\uDC65 "+status.getTotalOnline();

        if(status.getTotalOnline()==0){
            totalOnline="😴 " + status.getTotalOnline();
        }

        return new EmbedBuilder()
                .setTitle(TITLE)
                .setColor(status.getTotalOnline() > 0 ? Color.GREEN : Color.ORANGE)
                .setThumbnail(THUMBNAIL_URL)
                .addField("Общий онлайн",totalOnline, false)
                .addField("Онлайн по серверам", serversInfo, false)
                .setFooter("Данные полученные в реальном времени")
                .setTimestamp(Instant.now())
                .build();
    }

    public MessageEmbed createErrorEmbed(){
        return new EmbedBuilder()
                .setTitle(TITLE)
                .setColor(Color.RED)
                .setThumbnail(THUMBNAIL_URL)
                .setDescription("**Сервер недоступен**\nНе удалось получить информацию о статусе серверов. Попробуйте через несколько минут.")
                .setFooter("Возможно, сервер перезагружается или проводятся технические работы")
                .setTimestamp(Instant.now())
                .build();
    }
    private String formatServersInfo(NetworkStatusDto status){
        if (status.getServers()==null||status.getServers().isEmpty()){
            return "Нет данных по отдельным серверам";
        }
        return status.getServers().entrySet().stream()
                .map(entry -> {
                    String rawName = entry.getKey();
                    String prettyName = SERVER_NAME_MAPPINGS.getOrDefault(rawName, rawName);
                    return String.format("`%s`: **%d** игроков", prettyName, entry.getValue());
                })
                .collect(Collectors.joining("\n"));
    }
}
