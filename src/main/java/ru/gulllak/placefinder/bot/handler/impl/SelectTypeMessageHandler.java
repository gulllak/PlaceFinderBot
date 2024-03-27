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
import ru.gulllak.placefinder.service.ReplyMessageService;
import ru.gulllak.placefinder.util.Emoji;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SelectTypeMessageHandler implements MessageHandler {
    private final ReplyMessageService replyMessageService;

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.CHOOSING_TYPE);
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Message message) {
        long chatId = message.getChatId();

        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), message.getMessageId()-1);
        DeleteMessage deleteMessage1 = new DeleteMessage(String.valueOf(chatId), message.getMessageId());
        SendMessage sendMessage = replyMessageService.getTextMessage(chatId,
                "Хотите найти интересные достопримечательности или вкусно перекусить? \n Нажмите на кнопку и подождите. (Загрузка может выполняться до 10 секунд)" + Emoji.GHOST);

        ReplyKeyboard replyKeyboard = InlineKeyboardMarkupBuilder.create(chatId)
                .row()
                .button(Emoji.ATTRACTION + " Достопримечательности", "/attraction")
                .endRow()
                .row()
                .button(Emoji.PIZZA + " Кафе и рестораны", "/cafe")
                .endRow()
                .build();

        sendMessage.setReplyMarkup(replyKeyboard);

        return new ArrayList<>(Arrays.asList(deleteMessage, deleteMessage1, sendMessage));
    }
}
