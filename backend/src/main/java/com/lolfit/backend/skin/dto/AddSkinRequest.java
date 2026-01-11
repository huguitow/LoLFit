package com.lolfit.backend.skin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddSkinRequest {
    private Long skinId;
    private String acquisitionMethod;
}
