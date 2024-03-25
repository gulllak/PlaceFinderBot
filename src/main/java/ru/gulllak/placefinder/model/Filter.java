package ru.gulllak.placefinder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "filters")
public class Filter {
    @Id
    private Long userId;
    private int distance;
    private float raiting;
    private int reviewCount;
}
