package com.ovidius.persistence.repository;

import com.ovidius.persistence.model.Marriage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarriageRepository extends JpaRepository<Marriage, Long> {

    Optional<Marriage> findByFirstPartnerIdOrSecondPartnerId(String firstPartnerId, String secondPartnerId);
    boolean existsByFirstPartnerIdOrSecondPartnerId(String firstPartnerId, String secondPartnerId);
    List<Marriage> findAllByOrderByMarriageDateAsc(Pageable pageable);

}
