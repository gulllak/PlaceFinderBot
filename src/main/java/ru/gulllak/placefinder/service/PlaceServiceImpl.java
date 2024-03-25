package ru.gulllak.placefinder.service;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.gulllak.placefinder.model.Place;
import ru.gulllak.placefinder.mapper.PlaceMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    @Value("${api.key}")
    private String API_KEY;

    private final GeoApiContext context;

    @Override
    public PlacesSearchResponse getPlacesIdByText(GeoApiContext context, String messageText) {
        try {
            return PlacesApi.textSearchQuery(context, messageText).await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlaceDetails getPlaceDetailsByPlaceId(GeoApiContext context, String placeId) {
        try {
            return PlacesApi.placeDetails(context, placeId)
                    .language("ru")
                    .await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPlacePhoto(String photoReference) {
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=" + photoReference + "&key=" + API_KEY;
    }

    public PlacesSearchResponse getPlacesNearby(GeoApiContext context, LatLng location, PlaceType placeType, int distance) {
        try {
            return PlacesApi.nearbySearchQuery(context, location)
                    .type(placeType)
                    .radius(distance)
                    .language("ru")
                    .await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SendPhoto> answer(long chatId, String messageText) {
        //Получаем id текущего местоположения
        PlacesSearchResponse cityResponse = getPlacesIdByText(context, messageText);
        //Получаем список мест
        PlacesSearchResponse placesResponse =  getPlacesNearby(context, cityResponse.results[0].geometry.location, PlaceType.TOURIST_ATTRACTION, 5000);


        List<Place> places = PlaceMapper.placesSearchResponseToPlaces(placesResponse);

        List<SendPhoto> list = new ArrayList<>();

        if (places.size() > 0) {
            for (Place place : places) {
                list.add(prepareSendPhoto(place, chatId));
            }
        }
        return list;
    }

    public SendPhoto prepareSendPhoto(Place place, long chatId) {
        StringBuilder caption = new StringBuilder();
        String photo = getPlacePhoto(place.getPhotoReference());
        PlaceDetails placeDetails = getPlaceDetailsByPlaceId(context, place.getPlaceId());


        caption.append("<a href=\"").append(place.getIcon()).append("\">&#8205;</a>")
                .append("<b>").append(place.getName()).append("</b>").append("\n")
                .append("Рейтинг: ").append(place.getRating()).append("\n")
                .append("Количество отзывов: ").append(place.getReviewCount()).append("\n")
                .append("Открыто сейчас: ").append(isOpen(place.getOpenNow())).append("\n")
                .append("Адрес: ").append(place.getAddress());

        return SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(photo))
                .caption(caption.toString())
                .parseMode(ParseMode.HTML)
                .disableNotification(true)
                .build();
    }

    private String isOpen(Boolean openNow) {
        return openNow == null ? "Неизвестно" : openNow ? "Открыто" : "Закрыто";
    }
}
