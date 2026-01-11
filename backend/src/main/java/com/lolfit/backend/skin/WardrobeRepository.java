package com.lolfit.backend.skin;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WardrobeRepository extends JpaRepository<Wardrobe, Long> {
    List<Wardrobe> findByUserId(Long userId);

    public boolean existsByUserIdAndSkinId(Long userId, Long skinId);
}
