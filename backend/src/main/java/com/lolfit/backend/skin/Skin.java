package com.lolfit.backend.skin;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String riotId;

    private String name;
    private String rarity;
    private Integer rpPrice;
    private String imageUrl;
    private String splashArtUrl;
    private String theme;

    @ElementCollection
    @CollectionTable(name = "skin_tags", joinColumns = @JoinColumn(name = "skin_id"))
    @Column(name = "tag")
    private List<String> tags;

    private LocalDateTime releaseDate;

    @ManyToOne
    @JoinColumn(name = "champion_id", nullable = false)
    private Champion champion;
}