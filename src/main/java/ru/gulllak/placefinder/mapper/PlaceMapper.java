package ru.gulllak.placefinder.mapper;

import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import ru.gulllak.placefinder.model.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceMapper {
    private static Place placeSearchResultToPlace(PlacesSearchResult placesSearchResult) {
        return Place.builder()
                .placeId(placesSearchResult.placeId)
                .name(placesSearchResult.name)
                .icon(placesSearchResult.icon)
                .rating(placesSearchResult.rating)
                .types(Arrays.stream(placesSearchResult.types).toList())
                .openNow(placesSearchResult.openingHours != null ? placesSearchResult.openingHours.openNow : null)
                .photoReference(placesSearchResult.photos[0].photoReference)
                .address(placesSearchResult.vicinity)
                .reviewCount(placesSearchResult.userRatingsTotal)
                .build();
    }

    public static List<Place> placesSearchResponseToPlaces(PlacesSearchResponse placesSearchResponse) {
        List<Place> places = new ArrayList<>();
        for (PlacesSearchResult place : placesSearchResponse.results) {
            places.add(placeSearchResultToPlace(place));
        }
        return places;
    }
}
