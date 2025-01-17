package org.bekierz.savingstrackerbe.saving.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.bekierz.savingstrackerbe.user.model.entity.User;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "savings")
public class Saving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Positive
    private Double amount;

    @ManyToOne
    private User user;
    @ManyToOne
    private Asset asset;
}