package com.ovidius.adapter.services;

import com.ovidius.adapter.embeds.MarriageEmbedFactory;
import com.ovidius.persistence.model.CoupleInfo;
import com.ovidius.persistence.model.Marriage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CoupleTopService {

    private final MarriageManagerService marriageManager;

    public CoupleTopService(MarriageManagerService marriageManager) {
        this.marriageManager = marriageManager;
    }

    public CompletableFuture<List<CoupleInfo>> getTopCouplesInfo(Guild guild) {
        List<Marriage> topMarriages = marriageManager.getTopTenCouples();

        List<CompletableFuture<CoupleInfo>> futures = topMarriages.stream()
                .map(marriage -> {
                    CompletableFuture<Member> member1Future = guild.retrieveMemberById(marriage.getFirstPartnerId()).submit();
                    CompletableFuture<Member> member2Future = guild.retrieveMemberById(marriage.getSecondPartnerId()).submit();

                    return member1Future.thenCombine(member2Future, (m1, m2) -> {
                        String p1Name = (m1 != null) ? m1.getEffectiveName() : "Неизвестный";
                        String p2Name = (m2 != null) ? m2.getEffectiveName() : "Неизвестный";
                        String duration = MarriageEmbedFactory.formatDuration(marriage.getMarriageDate());
                        return new CoupleInfo(p1Name, p2Name, duration);
                    });
                }).toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }
}
