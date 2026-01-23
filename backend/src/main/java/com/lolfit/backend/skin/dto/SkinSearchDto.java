package com.lolfit.backend.skin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkinSearchDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String splashArtUrl;
    private String rarity;
    private Integer rpPrice;
    private String championName;
    private Long championId;
}
