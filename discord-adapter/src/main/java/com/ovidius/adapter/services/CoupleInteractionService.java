package com.ovidius.adapter.services;

import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

@Service
public class CoupleInteractionService {

    private final MarriageManagerService marriageManager;

    public CoupleInteractionService(MarriageManagerService marriageManager) {
        this.marriageManager = marriageManager;
    }

    public boolean canInteract(User initiator, User target) {
        if (initiator.equals(target)) return false;
        return marriageManager.findMarriageForUser(initiator.getId())
                .map(marriage -> {
                    String partner1Id = marriage.getFirstPartnerId();
                    String partner2Id = marriage.getSecondPartnerId();
                    String targetId = target.getId();
                    return (partner1Id.equals(targetId) || partner2Id.equals(targetId));
                })
                .orElse(false);
    }

}
