package com.lolfit.backend.skin.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class WardrobeResponse {
    private Long id;
    private Long skinId;
    private String skinName;
    private String championName;
    private String imageUrl;
    private LocalDateTime obtainedAt;
    private Boolean isFavorite;
}