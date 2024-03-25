package ru.gulllak.placefinder.model;

import lombok.Builder;
import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
@Builder
public class Place {
    private String placeId;
    private String name;
    private URL icon;
    private float rating;
    private List<String> types;
    private Boolean openNow;
    private String photoReference;
    private String address;
    private int reviewCount;
}
