package com.ovidius.persistence.repository;

import com.ovidius.persistence.model.Crest;
import com.ovidius.persistence.model.Marriage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrestRepository extends JpaRepository<Crest, Long> {
    Optional<Crest> findByTargetTypeAndTargetNameIgnoreCase(Crest.CrestTargetType targetType, String targetName);
}
