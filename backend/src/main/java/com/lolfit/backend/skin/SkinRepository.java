package com.lolfit.backend.skin;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SkinRepository extends JpaRepository<Skin, Long> {
    Optional<Skin> findByRiotId(String riotId);
}
