package ru.gulllak.placefinder.model;

import com.google.maps.model.PlaceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "filters")
public class Filter {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "place_type")
    PlaceType placeType;

    private int distance;

    private float rating;

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "current_lat")
    private double lat;

    @Column(name = "current_lon")
    private double lon;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    private User user;

    @Transient
    public boolean isDistance() {
        return distance != 0;
    }

    @Transient
    public boolean isRating() {
        return rating != 0.0f;
    }
}
