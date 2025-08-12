package com.ovidius.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "crests",
        uniqueConstraints = @UniqueConstraint(columnNames = {"target_type","target_name"})
)
@Data
@NoArgsConstructor
public class Crest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="target_type",nullable=false)
    @Enumerated(EnumType.STRING)
    private CrestTargetType targetType;

    @Column(name="target_name",nullable = false)
    private String targetName;

    @Column(name="image_url",nullable=false,length=512)
    private String imageUrl;

    public Crest(CrestTargetType targetType, String targetName, String imageUrl) {
        this.targetType = targetType;
        this.targetName = targetName;
        this.imageUrl = imageUrl;
    }

    public enum CrestTargetType {
        TOWN,
        NATION
    }

}
