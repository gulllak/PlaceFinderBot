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
import ru.gulllak.placefinder.service.ReplyMessageService;
import ru.gulllak.placefinder.util.Emoji;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartMessageHandler implements MessageHandler {
    private final ReplyMessageService replyMessageService;

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.MAIN_MENU);
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Message message) {
        Long chatId = message.getChatId();

        SendMessage sendMessage = replyMessageService.getTextMessage(chatId,
                String.join("\n\n",
                        "Привет " + getName(message) + "! " + Emoji.HELLO,
                        "Наш бот умеет искать наиболее интересные места рядом с тобой, а именно:",
                        Emoji.ATTRACTION + " Достопремечательности",
                        Emoji.PIZZA + " Рестораны\n",

                        Emoji.WORLD + " Поделитесть с нами своей геопозицией."));

        ReplyKeyboard replyKeyboard = InlineKeyboardMarkupBuilder.create(chatId)
                .setText("Вариант поиска")
                .row()
                .button("Поделиться моей локацией", "/location_search")
                .endRow()
//                .row()
//                .button("Ввести город", "/text_search")
//                .endRow()
//                , или отправьте название города сообщением
                .build();

        sendMessage.setReplyMarkup(replyKeyboard);

        return new ArrayList<>(Arrays.asList(sendMessage));
    }

    private String getName(Message message) {
        if (message.getFrom().getFirstName().equals("PlaceFinder")) {
            return message.getChat().getFirstName();
        } else {
            return message.getFrom().getFirstName();
        }
    }
}
