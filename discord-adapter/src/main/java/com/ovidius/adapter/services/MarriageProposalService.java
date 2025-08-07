package com.ovidius.adapter.services;

import com.ovidius.persistence.model.MarriageProposal;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class MarriageProposalService {

    private static final Logger log = LoggerFactory.getLogger(MarriageProposalService.class);

    private final Set<MarriageProposal> activeProposals = Collections.synchronizedSet(new HashSet<>());

    private final MarriageManagerService marriageManager;

    public MarriageProposalService(MarriageManagerService marriageManager) {
        this.marriageManager = marriageManager;
    }

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Optional<MarriageProposal> createProposal(User proposer, User receiver) {
        log.info("Attempting to create proposal from {} to {}", proposer.getName(), receiver.getName());
        if (proposer.equals(receiver) || receiver.isBot()) { return Optional.empty(); }
        if (isUserInvolvedInAnyProposal(proposer.getId()) || isUserInvolvedInAnyProposal(receiver.getId())) {
            log.warn("Proposal failed: A user is already involved in another proposal.");
            return Optional.empty();
        }
        if (marriageManager.isUserMarried(proposer.getId()) || marriageManager.isUserMarried(receiver.getId())) {
            log.warn("Proposal failed: One of the users is already married.");
            return Optional.empty();
        }
        MarriageProposal proposal = new MarriageProposal(proposer, receiver);
        activeProposals.add(proposal);
        scheduler.schedule(() -> {
            boolean removed = activeProposals.remove(proposal);
            if (removed) log.info("Removed expired proposal between {} and {}", proposer.getName(), receiver.getName());
        }, 5, TimeUnit.MINUTES);
        return Optional.of(proposal);
    }

    public void acceptProposal(MarriageProposal proposal) {
        marriageManager.createMarriage(proposal.getProposer(), proposal.getReceiver());
        activeProposals.remove(proposal);
    }
    public void declineProposal(MarriageProposal proposal) {
        activeProposals.remove(proposal);
    }
    public Optional<MarriageProposal> findProposalByMessageId(long messageId) {
        return activeProposals.stream()
                .filter(p -> p.getMessageId() == messageId)
                .findFirst();
    }
    private boolean isUserInvolvedInAnyProposal(String userId) {
        return activeProposals.stream()
                .anyMatch(p -> p.getProposer().getId().equals(userId) || p.getReceiver().getId().equals(userId));
    }
}
