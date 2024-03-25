package ru.gulllak.placefinder.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.gulllak.placefinder.bot.condition.BotConditionHandler;
import ru.gulllak.placefinder.model.User;
import ru.gulllak.placefinder.service.PlaceService;
import ru.gulllak.placefinder.service.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateReceiver {
    private final PlaceService placeService;

    private final UserService userService;

    private final BotConditionHandler botConditionHandler;

    public List<BotApiMethod<Message>> handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            BotCondition botCondition = getBotConditions(message);

//            if(message.getText().equals("/start")) {
//                return Collections.singletonList(startMessage(message.getChatId(), message.getFrom().getUserName()));
//            } else {
//                List<SendPhoto> sendPhotos = placeService.answer(message.getChatId(), message.getText());
//
//                return sendPhotos.stream().map(photo -> (PartialBotApiMethod<? extends Serializable>) photo).collect(Collectors.toList());
//            }

            return botConditionHandler.handleTextMessageByCondition(message, botCondition);
        }

        else if (update.hasMessage() && update.getMessage().hasLocation()) {
            Message message = update.getMessage();
            BotCondition botCondition = getBotConditions(message);

            return null;
        }


        else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = (Message) callbackQuery.getMessage();
            message.setText(callbackQuery.getData());

            BotCondition botCondition = getBotConditions(message);

            return botConditionHandler.handleTextMessageByCondition(message, botCondition);
        }

        else {
            return null;
        }
    }


    private SendMessage startMessage(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет " + userName + ". Введи название города по которому будет выполнен поиск");

        return message;
    }

    private BotCondition getBotConditions(Message message) {
        long userId = message.getFrom().getId();
        String textMessage = message.getText();

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
}
