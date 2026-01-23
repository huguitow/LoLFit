package com.lolfit.backend.skin;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WardrobeRepository extends JpaRepository<Wardrobe, Long> {
    List<Wardrobe> findByUserId(Long userId);

    boolean existsByUserIdAndSkinId(Long userId, Long skinId);

    void deleteByUserIdAndSkinId(Long userId, Long skinId);
}
