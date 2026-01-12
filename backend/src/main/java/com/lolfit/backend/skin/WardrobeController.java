package com.lolfit.backend.skin;

import com.lolfit.backend.skin.dto.AddSkinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.lolfit.backend.skin.dto.WardrobeResponse;
import java.util.List;

@RestController
@RequestMapping("/api/wardrobe")
@RequiredArgsConstructor
public class WardrobeController {
    private final WardrobeService wardrobeService;

    @PostMapping
    public ResponseEntity<String> addSkinToWardrobe(Authentication authentication,
            @RequestBody AddSkinRequest request) {
        String userEmail = authentication.getName();
        wardrobeService.addSkinToWardrobe(userEmail, request);
        return ResponseEntity.ok("Skin ajouté à la garde-robe");
    }

    @GetMapping
    public ResponseEntity<List<WardrobeResponse>> getMyWardrobe(Authentication authentication) {
        String userEmail = authentication.getName();
        List<WardrobeResponse> wardrobe = wardrobeService.getUserWardrobe(userEmail);
        return ResponseEntity.ok(wardrobe);
    }
}
