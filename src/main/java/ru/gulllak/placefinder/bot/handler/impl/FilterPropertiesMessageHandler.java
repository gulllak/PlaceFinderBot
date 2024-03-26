package ru.gulllak.placefinder.bot.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.gulllak.placefinder.bot.BotCondition;
import ru.gulllak.placefinder.bot.handler.MessageHandler;
import ru.gulllak.placefinder.bot.keyboard.InlineKeyboardMarkupBuilder;
import ru.gulllak.placefinder.model.Filter;
import ru.gulllak.placefinder.model.User;
import ru.gulllak.placefinder.service.ReplyMessageService;
import ru.gulllak.placefinder.service.UserService;
import ru.gulllak.placefinder.util.Emoji;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilterPropertiesMessageHandler implements MessageHandler {
    private final ReplyMessageService replyMessageService;

    private final UserService userService;

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.FILTER_PROPERTIES);
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        User user = userService.getById(chatId);
        Filter filter = user.getFilter();
        String filterState = user.getFilterState();


        SendMessage sendMessage = new SendMessage();
        ReplyKeyboard replyKeyboard;

        switch (filterState) {
            case "/radius" -> {
                int distance;
                try {
                    distance = Integer.parseInt(text);
                    if (distance < 0 || distance > 50000) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    return Collections.singletonList(replyMessageService.getTextMessage(chatId,
                            Emoji.ANGRY + " Введите целое число"));
                }
                filter.setDistance(distance);
                userService.save(user);

                if (filter.isRating()) {
                    sendMessage = replyMessageService.getTextMessage(chatId,
                            Emoji.OK + " Параметр «расстояние» сохранен. \n Нажмите «Продолжить»");
                    replyKeyboard = InlineKeyboardMarkupBuilder.create(chatId)
                            .row()
                            .button("Продолжить", "/continue")
                            .endRow()
                            .build();
                } else {
                    sendMessage = replyMessageService.getTextMessage(chatId,
                            Emoji.OK + " Параметр «расстояние» сохранен. \n Выберите один из доступных фильтров или нажмите «Продолжить»");

                    replyKeyboard = InlineKeyboardMarkupBuilder.create(chatId)
                            .row()
                            .button("Рейтинг", "/rating")
                            .endRow()
                            .row()
                            .button("Продолжить", "/continue")
                            .endRow()
                            .build();
                }
                sendMessage.setReplyMarkup(replyKeyboard);
                user.setFilterState(null);
            }
            case "/rating" -> {
                float rating;
                try {
                    rating = Float.parseFloat(text.replace(",", "."));
                    if (rating < 0 || rating > 5) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    return Collections.singletonList(replyMessageService.getTextMessage(chatId,
                            Emoji.ANGRY + " Введите число от 0 до 5, например 4.2"));
                }
                filter.setRating(rating);
                userService.save(user);

                if (filter.isDistance()) {
                    sendMessage = replyMessageService.getTextMessage(chatId,
                            Emoji.OK + " Параметр «рейтинг» сохранен. \n Нажмите «Продолжить»");

                    replyKeyboard = InlineKeyboardMarkupBuilder.create(chatId)
                            .row()
                            .button("Продолжить", "/continue")
                            .endRow()
                            .build();
                } else {
                    sendMessage = replyMessageService.getTextMessage(chatId,
                            Emoji.OK + " Параметр «рейтинг» сохранен. \n Выберите один из доступных фильтров или нажмите «Продолжить»");

                    replyKeyboard = InlineKeyboardMarkupBuilder.create(chatId)
                            .row()
                            .button("Расстояние", "/radius")
                            .endRow()
                            .row()
                            .button("Продолжить", "/continue")
                            .endRow()
                            .build();
                }
                sendMessage.setReplyMarkup(replyKeyboard);
                user.setFilterState(null);
            }
        }

        return Collections.singletonList(sendMessage);
    }
}
