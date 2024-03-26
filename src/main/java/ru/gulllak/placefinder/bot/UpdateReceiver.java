package ru.gulllak.placefinder.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.gulllak.placefinder.bot.condition.BotConditionHandler;
import ru.gulllak.placefinder.bot.handler.callbackquery.CallbackQueryHandler;
import ru.gulllak.placefinder.model.Filter;
import ru.gulllak.placefinder.model.User;
import ru.gulllak.placefinder.service.UserService;

import java.io.Serializable;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateReceiver {
    private final UserService userService;

    private final BotConditionHandler botConditionHandler;

    private final CallbackQueryHandler callbackQueryHandler;

    public List<PartialBotApiMethod<? extends Serializable>> handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            BotCondition botCondition = getBotConditions(message);

            log.info(
                    "Message from: {}; " +
                            "chat id: {};  " +
                            "text: {}; " +
                            "bot condition: {}",
                    message.getFrom().getId(),
                    message.getChatId(),
                    message.getText(),
                    botCondition
            );

            return botConditionHandler.handleTextMessageByCondition(message, botCondition);
        }

        else if (update.hasMessage() && update.getMessage().hasLocation()) {
            Message message = update.getMessage();
            message.setText("/searching_filters");
            BotCondition botCondition = getBotConditions(message);
            saveCoordinates(message);

            log.info(
                    "Message from: {}; " +
                            "chat id: {};  " +
                            "text: {}; " +
                            "bot condition: {}",
                    message.getFrom().getId(),
                    message.getChatId(),
                    message.getText(),
                    botCondition
            );

            //удаляем кнопку поделиться
            List<PartialBotApiMethod<? extends Serializable>> sending = botConditionHandler.handleTextMessageByCondition(message, botCondition);
            sending.add(0, removeKeyboard(message.getChatId()));

            return sending;
        }


        else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            log.info(
                    "CallbackQuery from: {}; " +
                            "data: {}; " +
                            "message id: {}",
                    callbackQuery.getFrom().getId(),
                    callbackQuery.getData(),
                    callbackQuery.getId()
            );

            return callbackQueryHandler.handleCallbackQuery(callbackQuery);
        }

        else {
            return null;
        }
    }

    private BotCondition getBotConditions(Message message) {
        long userId = message.getFrom().getId();
        String textMessage = message.getText() == null ? "default" : message.getText();

        User user = userService.getById(userId);

        BotCondition botCondition = switch (textMessage) {
            case "/start" -> BotCondition.MAIN_MENU;
            case "/text_search" -> BotCondition.TEXT_SEARCH;
            case "/location_search" -> BotCondition.LOCATION_SEARCH;
            case "/searching_filters" -> BotCondition.SEARCHING_FILTERS;
            case "/place_answer" -> BotCondition.PLACE_ANSWER;
            case "/help" -> BotCondition.HELP;
            default -> user != null ? user.getCondition() : BotCondition.MAIN_MENU;
        };
        updateCondition(userId, botCondition, user);
        return botCondition;
    }

    private void updateCondition(long userId, BotCondition condition, User user) {
        if (user == null) {
            user = new User();
            user.setUserId(userId);
        }
        user.setCondition(condition);

        userService.save(user);
    }

    private void saveCoordinates(Message message) {
        Location location = message.getLocation();
        User user = userService.getById(message.getChatId());

        Filter filter = new Filter();
        filter.setUserId(user.getUserId());
        filter.setLat(location.getLatitude());
        filter.setLon(location.getLongitude());
        filter.setUser(user);

        user.setFilter(filter);

        userService.save(user);
    }

    private SendMessage removeKeyboard(long chatId) {
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);

        SendMessage deleteKeyboard = new SendMessage();
        deleteKeyboard.setText("Спасибо за предоставленную локацию!");
        deleteKeyboard.setChatId(chatId);
        deleteKeyboard.setReplyMarkup(replyKeyboardRemove);

        return deleteKeyboard;
    }
}
