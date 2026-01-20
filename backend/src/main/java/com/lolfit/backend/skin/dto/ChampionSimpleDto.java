package com.lolfit.backend.skin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChampionSimpleDto {
    private Long id;
    private String name;
    private String title;
    private String imageUrl;
}