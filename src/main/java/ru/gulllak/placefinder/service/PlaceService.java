package ru.gulllak.placefinder.service;

import com.google.maps.GeoApiContext;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.util.List;

public interface PlaceService {

    PlacesSearchResponse getPlacesIdByText(GeoApiContext context, String messageText);

    PlaceDetails getPlaceDetailsByPlaceId(GeoApiContext context, String placeId);

    String getPlacePhoto(String photoReference);

    List<SendPhoto> answer(long chatId, String messageText);
}
