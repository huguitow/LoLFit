package com.lolfit.backend.skin.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChampionDetailDto {
    private Long id;
    private String name;
    private String title;
    private String imageUrl;
    private List<SkinDto> skins;
}
