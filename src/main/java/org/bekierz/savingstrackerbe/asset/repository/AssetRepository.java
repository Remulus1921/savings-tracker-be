package org.bekierz.savingstrackerbe.asset.repository;

import org.bekierz.savingstrackerbe.asset.model.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByCode(String code);
}
