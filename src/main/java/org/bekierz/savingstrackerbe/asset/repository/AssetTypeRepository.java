package org.bekierz.savingstrackerbe.asset.repository;

import org.bekierz.savingstrackerbe.asset.model.entity.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {
    AssetType findByName(String name);
}
