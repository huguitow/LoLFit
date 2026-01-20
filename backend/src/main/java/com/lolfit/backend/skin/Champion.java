package com.lolfit.backend.skin;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Champion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String riotId;

    @Column(unique = true, nullable = false)
    private String name;

    private String title;

    @Column(length = 5000)
    private String lore;

    private String rolePrincipal;
    private String imageUrl;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "champion")
    @JsonManagedReference
    private List<Skin> skins = new ArrayList<>();

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}