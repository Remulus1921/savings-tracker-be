package org.bekierz.savingstrackerbe.asset.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity(name = "assetTypes")
public class AssetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "assetType", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Asset> assets;
}
