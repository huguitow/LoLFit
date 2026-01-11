package com.lolfit.backend.skin;

import com.lolfit.backend.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wardrobe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "skin_id", nullable = false)
    private Skin skin;

    @Column(nullable = false)
    private LocalDateTime obtainedAt;

    private String acquisitionMethod;
    private Integer timesUsed;
    private Boolean isFavorite;

    @PrePersist
    void onCreate() {
        if (this.obtainedAt == null)
            this.obtainedAt = LocalDateTime.now();
        if (this.isFavorite == null)
            this.isFavorite = false;
    }
}