package ru.gulllak.placefinder.mapper;

import com.google.maps.model.PlaceDetails;
import ru.gulllak.placefinder.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceMapper {
    private static Place placeSearchResultToPlace(PlaceDetails placeDetails) {
        return Place.builder()
                .placeId(placeDetails.placeId)
                .name(placeDetails.name)
                .rating(placeDetails.rating)
                .openNow(placeDetails.openingHours != null ? placeDetails.openingHours.openNow : null)
                .photoReference(placeDetails.photos[0].photoReference)
                .address(placeDetails.vicinity)
                .reviewCount(placeDetails.userRatingsTotal)
                .source(placeDetails.url)
                .build();
    }

    public static List<Place> placesSearchResponseToPlaces(List<PlaceDetails> placeDetails) {
        List<Place> places = new ArrayList<>();
        for (PlaceDetails place : placeDetails) {
            places.add(placeSearchResultToPlace(place));
        }
        return places;
    }
}
