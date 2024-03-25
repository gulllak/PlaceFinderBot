package ru.gulllak.placefinder.bot.handler.callbackquery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gulllak.placefinder.bot.BotCondition;
import ru.gulllak.placefinder.bot.condition.BotConditionHandler;
import ru.gulllak.placefinder.model.User;
import ru.gulllak.placefinder.service.ReplyMessageService;
import ru.gulllak.placefinder.service.UserService;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlaceFinderCallbackQueryHandler implements CallbackQueryHandler {
    private final ReplyMessageService replyMessageService;

    private final UserService userService;

    private final BotConditionHandler botConditionHandler;

    @Override
    public List<BotApiMethod<? extends Serializable>> handleCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getFrom().getId();
        String callbackData = callbackQuery.getData();
        User user = userService.getById(chatId);
        Message message = getMessage(callbackQuery);
        SendMessage sendMessage = new SendMessage();

        switch (callbackData) {
            case "/location_search":
                user.setCondition(BotCondition.LOCATION_SEARCH);
                userService.save(user);

                message.setText(callbackData);

                return botConditionHandler.handleTextMessageByCondition(message, user.getCondition());
            case "/text_search": return null;

            case "/radius":
                user.setCondition(BotCondition.FILTER_PROPERTIES);
                user.setFilterState("/radius");
                userService.save(user);

                sendMessage = replyMessageService.getTextMessage(chatId,
                        "Укажите на каком максимальном расстоянии (в метрах) от вас искать");

                return Collections.singletonList(sendMessage);

            case "/rating":
                user.setCondition(BotCondition.FILTER_PROPERTIES);
                user.setFilterState("/rating");
                userService.save(user);

                sendMessage = replyMessageService.getTextMessage(chatId,
                        "Укажите минимальный рейтинг места от 0 до 5, например 4.2");

                return Collections.singletonList(sendMessage);

            case "/continue": return null;
            default: 
                return Collections.singletonList(
                        replyMessageService.getTextMessage(chatId, "Не удалось обрабоать информацию"));
        }
    }

    private Message getMessage(CallbackQuery callbackQuery) {
        return  (Message) callbackQuery.getMessage();
    }
}
