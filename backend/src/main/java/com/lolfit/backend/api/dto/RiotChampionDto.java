package com.lolfit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiotChampionDto {
    private String id;
    private String key;
    private String name;
    private String title;
    private String lore;
    private List<RiotSkinDto> skins;
}