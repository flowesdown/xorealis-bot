package com.ovidius.adapter.services;

import com.ovidius.persistence.model.Marriage;
import com.ovidius.persistence.repository.MarriageRepository;
import net.dv8tion.jda.api.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarriageManagerService {

    private final MarriageRepository marriageRepository;

    public MarriageManagerService(MarriageRepository marriageRepository) {
        this.marriageRepository = marriageRepository;
    }

    public Marriage createMarriage(User partner1, User partner2) {
        Marriage newMarriage = new Marriage(partner1.getId(), partner2.getId());
        return marriageRepository.save(newMarriage);
    }
    
    public void divorce(User user){
        findMarriageForUser(user.getId()).ifPresent(marriage -> {
            marriageRepository.delete(marriage);
        });
    }

    public boolean isUserMarried(String userId){
        return marriageRepository.existsByFirstPartnerIdOrSecondPartnerId(userId, userId);
    }
    public Optional<Marriage> findMarriageForUser(String userId){
        return marriageRepository.findByFirstPartnerIdOrSecondPartnerId(userId, userId);
    }
    public List<Marriage> getTopTenCouples(){
        Pageable limit = PageRequest.of(0, 10);
        return marriageRepository.findAllByOrderByMarriageDateAsc(limit);
    }

}
