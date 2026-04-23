package itsdax.kams.features.user.model.entity;


import itsdax.kams.core.constants.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Auditable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordHistory  extends BaseEntity{
    private String password; // BCrypt hash
    @ManyToOne
    private User user;
}
