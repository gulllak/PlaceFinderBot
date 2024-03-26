package ru.gulllak.placefinder.bot.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gulllak.placefinder.bot.BotCondition;
import ru.gulllak.placefinder.bot.handler.MessageHandler;
import ru.gulllak.placefinder.model.Filter;
import ru.gulllak.placefinder.model.User;
import ru.gulllak.placefinder.service.PlaceService;
import ru.gulllak.placefinder.service.UserService;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ResultMessageHandler implements MessageHandler {
    private final UserService userService;

    private final PlaceService placeService;

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.PLACE_ANSWER);
    }

    @Override
    public List<BotApiMethod<? extends Serializable>> handle(Message message) {
        Long chatId = message.getChatId();
        User user = userService.getById(chatId);
        Filter filter = user.getFilter();

        List<BotApiMethod<? extends Serializable>> searchResults = placeService.getSearchResults(chatId, filter);

        user.setCondition(BotCondition.MAIN_MENU);
        user.setFilter(null);

        return searchResults;
    }
}
