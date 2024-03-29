package ru.gulllak.placefinder.service;

import com.google.maps.GeoApiContext;
import com.google.maps.model.PlaceDetails;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import ru.gulllak.placefinder.model.Filter;

import java.io.Serializable;
import java.util.List;

public interface PlaceService {
    PlaceDetails getPlaceDetailsByPlaceId(GeoApiContext context, String placeId);

    String getPlacePhoto(String photoReference);

    List<PartialBotApiMethod<? extends Serializable>> getSearchResults(long chatId, Filter filter);
}
