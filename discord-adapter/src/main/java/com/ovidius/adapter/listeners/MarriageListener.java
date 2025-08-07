package com.ovidius.adapter.listeners;

import com.ovidius.adapter.embeds.MarriageEmbedFactory;
import com.ovidius.adapter.services.CoupleInteractionService;
import com.ovidius.adapter.services.MarriageManagerService;
import com.ovidius.adapter.services.MarriageProposalService;
import com.ovidius.persistence.model.Marriage;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class MarriageListener extends ListenerAdapter {

    private final MarriageProposalService proposalService;
    private final MarriageManagerService marriageManager;
    private final CoupleInteractionService interactionService;
    private final MarriageEmbedFactory embedFactory;

    public MarriageListener(
            MarriageProposalService proposalService,
            MarriageManagerService marriageManager,
            CoupleInteractionService interactionService,
            MarriageEmbedFactory embedFactory) {
        this.proposalService = proposalService;
        this.marriageManager = marriageManager;
        this.interactionService = interactionService;
        this.embedFactory = embedFactory;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "propose", "предложение" -> handlePropose(event);
            case "divorce", "расстаться" -> handleDivorce(event);
            case "couple", "пара" -> handleCoupleProfile(event);
            case "kiss", "поцеловать", "чмок" -> handleInteraction(event, "kiss");
            case "hug", "обнять" -> handleInteraction(event, "hug");
            case "couple-top", "топ пар" -> handleCoupleTop(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();
        if (componentId.startsWith("proposal-accept")) {
            handleProposalAccept(event);
        } else if (componentId.startsWith("proposal-decline")) {
            handleProposalDecline(event);
        }
    }

    private void handlePropose(SlashCommandInteractionEvent event) {
        User proposer = event.getUser();
        User receiver = Objects.requireNonNull(event.getOption("user")).getAsUser();

        proposalService.createProposal(proposer, receiver)
                .ifPresentOrElse(proposal -> {
                    String interactionId = event.getId();
                    event.replyEmbeds(embedFactory.createProposalEmbed(proposal))
                            .addActionRow(
                                    Button.success("proposal-accept:" + interactionId, "Принять"),
                                    Button.danger("proposal-decline:" + interactionId, "Отклонить")
                            )
                            .queue(response -> response.retrieveOriginal().queue(message -> {
                                proposal.setMessageId(message.getIdLong());
                                message.editMessageComponents().setComponents().queueAfter(5, TimeUnit.MINUTES);
                            }));
                }, () -> event.reply("❌ Невозможно сделать предложение. Убедитесь, что вы и выбранный пользователь свободны.").setEphemeral(true).queue());
    }
    private void handleDivorce(SlashCommandInteractionEvent event) {
        User initiator = event.getUser();
        Optional<Marriage> marriageOptional = marriageManager.findMarriageForUser(initiator.getId());

        if (marriageOptional.isPresent()) {
            Marriage marriage = marriageOptional.get();
            String partner1Id = marriage.getFirstPartnerId();
            String partner2Id = marriage.getSecondPartnerId();
            String exPartnerId = initiator.getId().equals(partner1Id) ? partner2Id : partner1Id;
            marriageManager.divorce(initiator);

            event.reply("💔 Ваш брак был успешно расторгнут.").setEphemeral(true).queue();
            event.getJDA().retrieveUserById(exPartnerId).queue(exPartner -> {
                String divorceMessage = String.format("💔 К сожалению, %s расторг(ла) ваш брак.", initiator.getName());
                exPartner.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(divorceMessage).queue());
            }, throwable -> System.out.println("Could not notify ex-partner about divorce."));
        } else {
            event.reply("❌ Вы не состоите в браке.").setEphemeral(true).queue();
        }
    }

    private void handleProposalDecline(ButtonInteractionEvent event) {
        proposalService.findProposalByMessageId(event.getMessageIdLong())
                .ifPresent(proposal -> {
                    if (!event.getUser().equals(proposal.getReceiver()) && !event.getUser().equals(proposal.getProposer())) {
                        event.reply("❌ Вы не являетесь участником этого предложения!").setEphemeral(true).queue();
                        return;
                    }
                    proposalService.declineProposal(proposal);
                    event.editMessageEmbeds(embedFactory.createProposalDeclinedEmbed(proposal.getProposer(), proposal.getReceiver()))
                            .setComponents().queue();
                });
    }


    private void handleCoupleProfile(SlashCommandInteractionEvent event) {
        User target = event.getOption("user", event.getUser(), OptionMapping::getAsUser);

        marriageManager.findMarriageForUser(target.getId())
                .ifPresentOrElse(marriage -> {
                    event.getJDA().retrieveUserById(marriage.getFirstPartnerId()).queue(partner1 -> {
                        event.getJDA().retrieveUserById(marriage.getSecondPartnerId()).queue(partner2 -> {
                            event.replyEmbeds(embedFactory.createCoupleProfileEmbed(marriage, partner1, partner2)).queue();
                        });
                    });

                }, () -> {
                    event.reply("💔 Пользователь " + target.getName() + " не состоит в браке.").setEphemeral(true).queue();
                });
    }

    private void handleInteraction(SlashCommandInteractionEvent event, String type) {
        User initiator = event.getUser();
        User target = Objects.requireNonNull(event.getOption("partner")).getAsUser();

        if (interactionService.canInteract(initiator, target)) {
            String replyMessage = switch (type) {
                case "kiss" -> embedFactory.getRandomKissPhrase(initiator, target);
                case "hug" -> embedFactory.getRandomHugPhrase(initiator, target);
                default -> "Неизвестное действие.";
            };
            event.reply(replyMessage).queue();
        } else {
            String errorMessage = "❌ Вы можете %s только своего партнера по браку!".formatted(
                    type.equals("kiss") ? "поцеловать" : "обнять"
            );
            event.reply(errorMessage).setEphemeral(true).queue();
        }
    }

    private void handleCoupleTop(SlashCommandInteractionEvent event) {
        event.replyEmbeds(embedFactory.createCouplesTopEmbed(marriageManager.getTopTenCouples(), event.getJDA())).queue();
    }


    private void handleProposalAccept(ButtonInteractionEvent event) {
        proposalService.findProposalByMessageId(event.getMessageIdLong())
                .ifPresent(proposal -> {
                    if (!event.getUser().equals(proposal.getReceiver())) {
                        event.reply("❌ Это предложение не для вас!").setEphemeral(true).queue();
                        return;
                    }
                    proposalService.acceptProposal(proposal);
                    event.editMessageEmbeds(embedFactory.createProposalAcceptedEmbed(proposal.getProposer(), proposal.getReceiver()))
                            .setComponents().queue();
                });
    }
}



