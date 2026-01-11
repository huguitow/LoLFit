package com.lolfit.backend.skin;

import org.springframework.stereotype.Service;
import com.lolfit.backend.skin.dto.AddSkinRequest;
import com.lolfit.backend.user.User;
import com.lolfit.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WardrobeService {
    private final UserRepository userRepository;
    private final WardrobeRepository wardrobeRepository;
    private final SkinRepository skinRepository;

    public void addSkinToWardrobe(String userEmail, AddSkinRequest request) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Skin skin = skinRepository.findById(request.getSkinId())
                .orElseThrow(() -> new RuntimeException("Skin not found"));
        boolean exists = wardrobeRepository.existsByUserIdAndSkinId(user.getId(), skin.getId());
        if (exists) {
            throw new RuntimeException("Skin already in wardrobe");
        }
        Wardrobe entry = Wardrobe.builder()
                .user(user)
                .skin(skin)
                .obtainedAt(LocalDateTime.now())
                .isFavorite(false)
                .acquisitionMethod(request.getAcquisitionMethod())
                .build();

        wardrobeRepository.save(entry);
    }
}
