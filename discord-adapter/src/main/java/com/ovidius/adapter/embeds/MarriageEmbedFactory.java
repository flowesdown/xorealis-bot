package com.ovidius.adapter.embeds;

import com.ovidius.persistence.model.Marriage;
import com.ovidius.persistence.model.MarriageProposal;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
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
        String duration = formatDuration(marriage.getMarriageDate());

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

    public MessageEmbed createCouplesTopEmbed(List<Marriage> topCouples, JDA jda) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(MARRIAGE_COLOR)
                .setTitle("\uD83D\uDC96 Топ-10 самых долгих браков");
        if (topCouples.isEmpty()) {
            embed.setDescription("На сервере пока нет ни одной пары!");
            return embed.build();
        }

        StringBuilder description = new StringBuilder();
        int rank = 1;
        for (Marriage marriage : topCouples) {
            User p1 = jda.getUserById(marriage.getFirstPartnerId());
            User p2 = jda.getUserById(marriage.getSecondPartnerId());

            String p1Name = (p1 != null) ? p1.getName() : "Неизвестно";
            String p2Name = (p2 != null) ? p2.getName() : "Неизвестно";
            String duration = formatDuration(marriage.getMarriageDate());

            String medal = switch(rank){
                case 1 -> "🥇";
                case 2 -> "🥈";
                case 3 -> "🥉";
                default -> String.format("**%d.**",rank);
            };
            description.append(String.format("%s `%s` и `%s` (%s)\n", medal, p1Name, p2Name, formatDuration(marriage.getMarriageDate())));
            rank++;
        }
        embed.setDescription(description.toString());
        return embed.build();
    }
    private static String formatDuration(LocalDateTime since) {
        Duration duration = Duration.between(since, LocalDateTime.now());
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();

        if(days > 0) return String.format("%d дн. %d ч.", days, hours);
        if(hours > 0) return String.format("%d ч. %d мин.", hours, minutes);
        return String.format("%d мин.", minutes);
    }

}
