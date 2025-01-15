package org.bekierz.savingstrackerbe.saving.repository;

import org.bekierz.savingstrackerbe.saving.model.entity.Saving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long> {
    List<Saving> findByUserEmail(String email);
    Saving findSavingByUserEmailAndAssetCode(String email, String assetCode);
}
