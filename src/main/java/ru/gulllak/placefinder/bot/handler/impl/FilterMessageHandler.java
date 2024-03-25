package ru.gulllak.placefinder.bot.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
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
public class FilterMessageHandler implements MessageHandler {
    private final ReplyMessageService replyMessageService;

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.SEARCHING_FILTERS);
    }

    @Override
    public List<BotApiMethod<? extends Serializable>> handle(Message message) {
        Long chatId = message.getChatId();

        SendMessage sendMessage = replyMessageService.getTextMessage(chatId,
                Emoji.MAG + " Здесь вы можете добавить поисковые фильтры или получить результат без фильтров.");

        ReplyKeyboard replyKeyboard = InlineKeyboardMarkupBuilder.create(chatId)
                .row()
                .button("Расстояние", "/radius")
                .button("Рейтинг", "/rating")
                .endRow()
                .row()
                .button("Продолжить", "/continue")
                .endRow()
                .build();

        sendMessage.setReplyMarkup(replyKeyboard);

        return new ArrayList<>(Arrays.asList(sendMessage));
    }
}
