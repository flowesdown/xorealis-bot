package com.ovidius.adapter.services;

import com.ovidius.adapter.embeds.StatusEmbedFactory;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Service;

import com.ovidius.minecraft.client.services.XorealisBridgeClient;

@Service
public class ServerInfoService {

    private final XorealisBridgeClient bridgeClient;

    private final StatusEmbedFactory embedFactory;

    public ServerInfoService(XorealisBridgeClient bridgeClient,StatusEmbedFactory embedFactory) {
        this.bridgeClient = bridgeClient;
        this.embedFactory = embedFactory;
    }

    public MessageEmbed createStatusEmbed() {
        return bridgeClient.getNetworkStatus()
                .map(embedFactory::createSuccessEmbed)
                .orElseGet(embedFactory::createErrorEmbed);
    }

}
