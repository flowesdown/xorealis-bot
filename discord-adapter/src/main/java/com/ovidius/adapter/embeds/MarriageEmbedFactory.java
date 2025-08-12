package com.ovidius.adapter.embeds;

import com.ovidius.persistence.model.CoupleInfo;
import com.ovidius.persistence.model.Marriage;
import com.ovidius.persistence.model.MarriageProposal;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;


@Component
public class MarriageEmbedFactory {

    private static final Color MARRIAGE_COLOR = new Color(255, 105, 180);

    private static final String DEFAULT_THUMBNAIL = "https://i.imgur.com/l8AMQG5.png";

    private static final List<String> KISS_PHRASES = List.of(
            "💋 %s нежно целует %s!",
            "😘 %s отправляет воздушный поцелуй %s!",
            "❤️ %s чмокает в щёчку %s!",
            "💕 %s оставляет сладкий поцелуй на губах %s."
    );
    private static final List<String> HUG_PHRASES = List.of(
            "🫂 %s крепко обнимает %s!",
            "🤗 %s заключил(а) %s в тёплые объятия.",
            "🥰 %s уютно прижимается к %s.",
            "💖 %s дарит тёплые обнимашки %s!"
    );

    private static final Random random = new Random();

    public String getRandomKissPhrase(User initiator, User target) {
        String phraseTemplate = KISS_PHRASES.get(random.nextInt(KISS_PHRASES.size()));
        return String.format(phraseTemplate, initiator.getAsMention(), target.getAsMention());
    }

    public String getRandomHugPhrase(User initiator, User target) {
        String phraseTemplate = HUG_PHRASES.get(random.nextInt(HUG_PHRASES.size()));
        return String.format(phraseTemplate, initiator.getAsMention(), target.getAsMention());
    }

    public MessageEmbed createProposalEmbed(MarriageProposal proposal) {
        User proposer = proposal.getProposer();
        User receiver = proposal.getReceiver();

        return new EmbedBuilder()
                .setColor(MARRIAGE_COLOR)
                .setAuthor(proposer.getName() + " делает предложение! ", null,
                        proposer.getEffectiveAvatarUrl())
                .setDescription("**" + receiver.getAsMention() + "**, вы примете это предложение?")
                .setThumbnail(receiver.getEffectiveAvatarUrl())
                .setFooter("Предложение действует 5 минут")
                .build();
    }

    public MessageEmbed createProposalAcceptedEmbed(User proposer, User receiver) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("\uD83D\uDC8D Совет да любовь!")
                .setDescription(proposer.getAsMention() + " и " + receiver.getAsMention() + " теперь состоят в браке!")
                .setThumbnail("https://cdn-icons-png.flaticon.com/512/6648/6648895.png")
                .setTimestamp(Instant.now())
                .build();
    }

    public MessageEmbed createProposalDeclinedEmbed(User proposer, User receiver) {
        return new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle("\uD83D\uDC94 Предложение отклонено")
                .setDescription(receiver.getAsMention() + " отклонила(а) предложение " + proposer.getAsMention() + ".")
                .build();
    }

    public MessageEmbed createCoupleProfileEmbed(Marriage marriage, User partner1, User partner2) {
        String duration = MarriageEmbedFactory.formatDuration(marriage.getMarriageDate());

        return new EmbedBuilder()
                .setColor(MARRIAGE_COLOR)
                .setAuthor("Профиль пары", null, partner1.getEffectiveAvatarUrl())
                .setThumbnail(partner2.getEffectiveAvatarUrl())
                .addField("Партнеры", partner1.getAsMention() + " & " + partner2.getAsMention(), false)
                .addField("Вместе уже", "💞 " + duration, false)
                .setTimestamp(marriage.getMarriageDate().atZone(java.time.ZoneId.systemDefault()).toInstant())
                .build();
    }

    public MessageEmbed createDivorceEmbed(User initiator, User exPartner) {
        return new EmbedBuilder()
                .setColor(Color.GRAY)
                .setTitle("\uD83D\uDC94 Брак расторгнут")
                .setDescription(initiator.getAsMention() + " и " + exPartner.getAsMention() + " больше не вместе.")
                .build();
    }

    public MessageEmbed createCouplesTopEmbed(List<CoupleInfo> topCouples) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(MARRIAGE_COLOR)
                .setTitle("💖 Топ-10 самых долгих браков");

        if (topCouples.isEmpty()) { /* ... */ }

        StringBuilder description = new StringBuilder();
        for (int i = 0; i < topCouples.size(); i++) {
            CoupleInfo couple = topCouples.get(i);
            String medal = getMedal(i + 1); // <-- ВАШ ЗАПРОС
            description.append(String.format("%s `%s` и `%s` (%s)\n",
                    medal, couple.partner1Name(), couple.partner2Name(), couple.duration()));
        }
        embed.setDescription(description.toString());
        return embed.build();
    }
    public static String formatDuration(LocalDateTime since) {
        Duration duration = Duration.between(since, LocalDateTime.now());
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();

        if(days > 0) return String.format("%d дн. %d ч.", days, hours);
        if(hours > 0) return String.format("%d ч. %d мин.", hours, minutes);
        return String.format("%d мин.", minutes);
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
