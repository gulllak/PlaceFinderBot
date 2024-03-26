package ru.gulllak.placefinder.bot.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.gulllak.placefinder.bot.BotCondition;
import ru.gulllak.placefinder.bot.handler.MessageHandler;
import ru.gulllak.placefinder.bot.keyboard.InlineKeyboardMarkupBuilder;
import ru.gulllak.placefinder.model.Filter;
import ru.gulllak.placefinder.model.User;
import ru.gulllak.placefinder.service.PlaceService;
import ru.gulllak.placefinder.service.ReplyMessageService;
import ru.gulllak.placefinder.service.UserService;
import ru.gulllak.placefinder.util.Emoji;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ResultMessageHandler implements MessageHandler {
    private final UserService userService;

    private final PlaceService placeService;

    private final ReplyMessageService replyMessageService;

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.PLACE_ANSWER);
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Message message) {
        Long chatId = message.getChatId();
        User user = userService.getById(chatId);
        Filter filter = user.getFilter();

        List<PartialBotApiMethod<? extends Serializable>> searchResults = placeService.getSearchResults(chatId, filter);

        user.setCondition(BotCondition.MAIN_MENU);
        user.setFilter(null);
        userService.save(user);

        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), message.getMessageId());

        SendMessage sendMessage = replyMessageService.getTextMessage(chatId,
                Emoji.MAG + " Начать новый поиск?");

        ReplyKeyboard replyKeyboard = InlineKeyboardMarkupBuilder.create(chatId)
                .row()
                .button("Начать сначала", "/start")
                .endRow()
                .row()
                .build();

        sendMessage.setReplyMarkup(replyKeyboard);
        searchResults.add(0, deleteMessage);
        searchResults.add(sendMessage);

        return searchResults;
    }
}
