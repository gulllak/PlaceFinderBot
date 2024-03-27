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
import ru.gulllak.placefinder.bot.keyboard.ReplyKeyboardMarkupBuilder;
import ru.gulllak.placefinder.service.ReplyMessageService;
import ru.gulllak.placefinder.util.Emoji;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LocationMessageHandler implements MessageHandler {
    private final ReplyMessageService replyMessageService;

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.LOCATION_SEARCH);
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Message message) {
        Long chatId = message.getChatId();
        //Удаление предыдущего сообщения
        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), message.getMessageId());

        SendMessage sendMessage = replyMessageService.getTextMessage(chatId, Emoji.LOCATION + " Нажмите на кнопку поделится локацией " + Emoji.DOWN);


        ReplyKeyboard replyKeyboard = ReplyKeyboardMarkupBuilder.create(chatId)
                .row()
                .button("Поделиться геолокацией")
                .endRow()
                .build();


        sendMessage.setReplyMarkup(replyKeyboard);

        return new ArrayList<>(Arrays.asList(deleteMessage, sendMessage));
    }
}
