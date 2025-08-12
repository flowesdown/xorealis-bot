package com.ovidius.adapter.services;

import com.ovidius.persistence.model.Crest;
import com.ovidius.persistence.repository.CrestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CrestService {
    private final CrestRepository crestRepository;

    public CrestService(CrestRepository crestRepository) {
        this.crestRepository = crestRepository;
    }

    @Transactional
    public void setCrest(Crest.CrestTargetType type, String name, String url) {
        Crest crest = crestRepository.findByTargetTypeAndTargetNameIgnoreCase(type, name)
                .orElse(new Crest(type, name, url));
        crest.setImageUrl(url);
        crestRepository.save(crest);
    }

    @Transactional(readOnly = true)
    public Optional<String> getCrestUrl(Crest.CrestTargetType type, String name) {
        return crestRepository.findByTargetTypeAndTargetNameIgnoreCase(type, name)
                .map(Crest::getImageUrl);
    }
}
