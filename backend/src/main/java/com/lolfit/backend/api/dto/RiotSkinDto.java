package com.lolfit.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiotSkinDto {
    private String id;
    private String name;
    private int num;
}
