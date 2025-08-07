package com.ovidius.persistence.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import net.dv8tion.jda.api.entities.User;

@Getter
@EqualsAndHashCode(of = {"proposer", "receiver"})
public class MarriageProposal {

    private final User proposer;
    private final User receiver;
    private long messageId;

    public MarriageProposal(User proposer, User receiver) {
        this.proposer = proposer;
        this.receiver = receiver;
        this.messageId = 0L;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}
