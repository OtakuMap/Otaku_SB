package com.otakumap.domain.place_like.entity;

import com.otakumap.domain.mapping.PlaceAnimation;
import com.otakumap.domain.place.entity.Place;
import com.otakumap.domain.user.entity.User;
import com.otakumap.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlaceLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_animation_id", nullable = false)
    private PlaceAnimation placeAnimation;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "is_favorite", nullable = false)
    @ColumnDefault("false")
    private Boolean isFavorite;

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}