package com.ovidius.adapter.services;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class ServerInfoService {

    public MessageEmbed createServerInfoEmbed() {
        String ip = "mc.xorealis.com";
        String status = "Online";
        int onlinePlayers = 27;
        int maxPlayers = 29;

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Информация о сервере Xorealis");
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setThumbnail("https://easydonate.s3.easyx.ru/images/logos/58/e2/58e26009a0104812d144b733899dfb37816dcd0a7fea8e85b6eafb07b78dc2fc.png");

        embedBuilder.addField("Статус", status, true);
        embedBuilder.addField("Игроков",onlinePlayers+" / "+maxPlayers, true);
        embedBuilder.addField("IP адрес","'"+ip+"'",false);

        embedBuilder.setFooter("Информация актуальная на данный момент");
        return embedBuilder.build();
    }
}
