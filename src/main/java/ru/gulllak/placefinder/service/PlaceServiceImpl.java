package ru.gulllak.placefinder.service;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.gulllak.placefinder.model.Filter;
import ru.gulllak.placefinder.model.Place;
import ru.gulllak.placefinder.mapper.PlaceMapper;
import ru.gulllak.placefinder.util.AttractionTypes;
import ru.gulllak.placefinder.util.CafeTypes;
import ru.gulllak.placefinder.util.Emoji;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    @Value("${GOOGLE_PLACES_API_KEY}")
    private String API_KEY;

    private final GeoApiContext context;

    private final ReplyMessageService replyMessageService;

    @Override
    public PlaceDetails getPlaceDetailsByPlaceId(GeoApiContext context, String placeId) {
        try {
            return PlacesApi.placeDetails(context, placeId)
                    .language("ru")
                    .fields(PlaceDetailsRequest.FieldMask.PLACE_ID, PlaceDetailsRequest.FieldMask.NAME,
                            PlaceDetailsRequest.FieldMask.RATING, PlaceDetailsRequest.FieldMask.OPENING_HOURS,
                            PlaceDetailsRequest.FieldMask.PHOTOS, PlaceDetailsRequest.FieldMask.VICINITY,
                            PlaceDetailsRequest.FieldMask.USER_RATINGS_TOTAL, PlaceDetailsRequest.FieldMask.URL)
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
    public List<PartialBotApiMethod<? extends Serializable>> getSearchResults(long chatId, Filter filter) {
        LatLng latLng = new LatLng(filter.getLat(), filter.getLon());
        List<PlacesSearchResult> results = new ArrayList<>();
        List<PlaceDetails> placeWithDetails = new ArrayList<>();

        if (filter.getPlaceType().equals(PlaceType.CAFE)) {
            results = getAllTypePlaces(context, latLng, getDistance(filter), CafeTypes.class);
        }

        if(filter.getPlaceType().equals(PlaceType.TOURIST_ATTRACTION)) {
            results = getAllTypePlaces(context, latLng, getDistance(filter), AttractionTypes.class);
        }

        for (PlacesSearchResult result : results) {
            placeWithDetails.add(getPlaceDetailsByPlaceId(context, result.placeId));
        }

        placeWithDetails = placeWithDetails.stream()
                .filter(placeDetails -> placeDetails.photos != null)
                .collect(Collectors.toList());

        Comparator<Place> reviewCountComparator = Comparator.comparingInt(Place::getReviewCount).reversed();
        Comparator<Place> ratingComparator = Comparator.comparingDouble(Place::getRating).reversed();

        List<Place> places = PlaceMapper.placesSearchResponseToPlaces(placeWithDetails).stream()
                .filter(place -> place.getRating() >= filter.getRating())
                .sorted(reviewCountComparator.thenComparing(ratingComparator))
                .limit(15)
                .toList();

        List<PartialBotApiMethod<? extends Serializable>> result = new ArrayList<>();

        if (places.size() > 0) {
            for (Place place : places) {
                result.add(prepareSendPhoto(place, chatId));
            }
        }

        if(result.size() == 0) {
            SendMessage sendMessage = replyMessageService.getTextMessage(chatId,
                    "К сожалению мы не нашли интересных локаций по вашему запросу " + Emoji.CRYING_CAT);
            result.add(sendMessage);
        }

        return result;
    }

    public SendPhoto prepareSendPhoto(Place place, long chatId) {
        StringBuilder caption = new StringBuilder();
        String photo = getPlacePhoto(place.getPhotoReference());

        caption
                .append("<b>").append(place.getName()).append("</b>").append("\n")
                .append("Рейтинг: ").append(place.getRating()).append("\n")
                .append("Количество отзывов: ").append(place.getReviewCount()).append("\n")
                .append("Открыто сейчас: ").append(isOpen(place.getOpenNow())).append("\n")
                .append("Адрес: ").append(place.getAddress()).append("\n")
                .append("<a href=\"").append(place.getSource().toString()).append("\"><b>Перейти в карты</b></a>");

        return SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(photo))
                .caption(caption.toString())
                .parseMode(ParseMode.HTML)
                .disableNotification(true)
                .build();
    }

    private String isOpen(Boolean openNow) {
        return openNow == null ? "Неизвестно" : "Открыто";
    }

    private <T extends Enum<T>> List<PlacesSearchResult> getAllTypePlaces(GeoApiContext context, LatLng latLng, int distance, Class<T> types) {
        Map<String, PlacesSearchResult> uniqueResults = new HashMap<>();

        for (T type : EnumSet.allOf(types)) {
            PlaceType placeType = PlaceType.valueOf(type.name());
            PlacesSearchResponse response = getPlacesNearby(context, latLng, placeType, distance);

            for(PlacesSearchResult place : response.results) {
                //добавляем только открытые или неизвестно
                if(place.openingHours == null || place.openingHours.openNow) {
                    uniqueResults.put(place.placeId, place);
                }
            }
        }

        return new ArrayList<>(uniqueResults.values());
    }

    private int getDistance(Filter filter) {
        return filter.getDistance() == 0 ? 10000 : filter.getDistance();
    }
}
