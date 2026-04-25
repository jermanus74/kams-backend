package itsdax.kams.features.assets.model.entity;


import itsdax.kams.core.constants.BaseEntity;
import itsdax.kams.features.assets.model.enums.AssetCondition;
import itsdax.kams.features.assets.model.enums.AssetStatus;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Assets extends BaseEntity {
    // Getters and Setters
    private String name;
    private String description;
    private AssetStatus status;
    private AssetCondition condition;

}