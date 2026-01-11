package com.lolfit.backend.skin;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChampionRepository extends JpaRepository<Champion, Long> {
    Optional<Champion> findByRiotId(String riotId);
}
