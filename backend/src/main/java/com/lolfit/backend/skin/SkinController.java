package com.lolfit.backend.skin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/skins")
@RequiredArgsConstructor
public class SkinController {
    private final SkinRepository skinRepository;
    private final ChampionRepository championRepository;

    @GetMapping("/champions")
    public ResponseEntity<List<Champion>> getAllChampions() {
        return ResponseEntity.ok(championRepository.findAll());
    }

    @GetMapping
    public ResponseEntity<List<Skin>> searchSkin(@RequestParam(required = false) String search) {
        if (search == null || search.isEmpty()) {
            return ResponseEntity.ok(skinRepository.findAll().stream().limit(20).toList());
        }
        List<Skin> found = skinRepository.findAll().stream()
                .filter(s -> s.getName().toLowerCase().contains(search.toLowerCase())
                        || s.getChampion().getName().toLowerCase().contains(search.toLowerCase()))
                .limit(20)
                .toList();
        return ResponseEntity.ok(found);
    }
}
