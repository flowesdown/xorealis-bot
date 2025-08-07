package com.ovidius.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "marriages")
@Data
@NoArgsConstructor
public class Marriage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_partner_id", nullable = false, unique = true)
    private String firstPartnerId;

    @Column(name = "second_partner_id", nullable = false, unique = true)
    private String secondPartnerId;

    @Column(name="marriage_date", nullable = false)
    private LocalDateTime marriageDate;

    public Marriage(String firstPartnerId, String secondPartnerId) {
        this.firstPartnerId = firstPartnerId;
        this.secondPartnerId = secondPartnerId;
        this.marriageDate = LocalDateTime.now();
    }
}
