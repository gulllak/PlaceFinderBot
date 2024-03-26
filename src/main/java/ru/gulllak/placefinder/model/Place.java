package ru.gulllak.placefinder.model;

import lombok.Builder;
import lombok.Data;

import java.net.URL;

@Data
@Builder
public class Place {
    private String placeId;
    private String name;
    private float rating;
    private Boolean openNow;
    private String photoReference;
    private String address;
    private int reviewCount;
    private URL source;
}
