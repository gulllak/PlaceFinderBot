package ru.gulllak.placefinder.configuration;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.gulllak.placefinder.bot.PlaceFinderBot;

@Configuration
public class PlaceFinderBotConfiguration {

    @Bean
    public GeoApiContext context(@Value("${api.key}") String apiKey) {
        return new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(PlaceFinderBot placeFinderBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(placeFinderBot);
        return api;
    }
}
