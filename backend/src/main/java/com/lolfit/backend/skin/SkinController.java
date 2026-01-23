package com.lolfit.backend.skin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.lolfit.backend.skin.dto.ChampionSimpleDto;
import com.lolfit.backend.skin.dto.ChampionDetailDto;
import com.lolfit.backend.skin.dto.SkinDto;
import com.lolfit.backend.skin.dto.SkinSearchDto;

import java.util.List;

@RestController
@RequestMapping("/api/skins")
@RequiredArgsConstructor
public class SkinController {
        private final SkinRepository skinRepository;
        private final ChampionRepository championRepository;

        @GetMapping("/champions")
        public ResponseEntity<List<ChampionSimpleDto>> getAllChampions() {
                List<ChampionSimpleDto> champions = championRepository.findAll().stream()
                                .map(c -> ChampionSimpleDto.builder()
                                                .id(c.getId())
                                                .name(c.getName())
                                                .title(c.getTitle())
                                                .imageUrl(c.getImageUrl())
                                                .build())
                                .toList();
                return ResponseEntity.ok(champions);
        }

        @GetMapping("/champions/{id}")
        @Transactional(readOnly = true)
        public ResponseEntity<ChampionDetailDto> getChampionById(@PathVariable Long id) {
                Champion champion = championRepository.findById(id).orElse(null);
                if (champion == null) {
                        return ResponseEntity.notFound().build();
                }

                List<SkinDto> skinDtos = champion.getSkins().stream()
                                .map(skin -> SkinDto.builder()
                                                .id(skin.getId())
                                                .name(skin.getName())
                                                .imageUrl(skin.getImageUrl())
                                                .splashArtUrl(skin.getSplashArtUrl())
                                                .rarity(skin.getRarity())
                                                .rpPrice(skin.getRpPrice())
                                                .build())
                                .toList();

                ChampionDetailDto dto = ChampionDetailDto.builder()
                                .id(champion.getId())
                                .name(champion.getName())
                                .title(champion.getTitle())
                                .imageUrl(champion.getImageUrl())
                                .skins(skinDtos)
                                .build();

                return ResponseEntity.ok(dto);
        }

        @GetMapping
        public ResponseEntity<List<SkinSearchDto>> searchSkin(@RequestParam(required = false) String search) {
                List<Skin> skins;
                if (search == null || search.isEmpty()) {
                        skins = skinRepository.findAll().stream().limit(20).toList();
                } else {
                        skins = skinRepository.findAll().stream()
                                        .filter(s -> s.getName().toLowerCase().contains(search.toLowerCase())
                                                        || s.getChampion().getName().toLowerCase()
                                                                        .contains(search.toLowerCase()))
                                        .limit(20)
                                        .toList();
                }

                List<SkinSearchDto> result = skins.stream()
                                .map(skin -> SkinSearchDto.builder()
                                                .id(skin.getId())
                                                .name(skin.getName())
                                                .imageUrl(skin.getImageUrl())
                                                .splashArtUrl(skin.getSplashArtUrl())
                                                .rarity(skin.getRarity())
                                                .rpPrice(skin.getRpPrice())
                                                .championName(skin.getChampion().getName())
                                                .championId(skin.getChampion().getId())
                                                .build())
                                .toList();

                return ResponseEntity.ok(result);
        }
}
