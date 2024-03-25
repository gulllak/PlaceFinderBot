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
    public List<BotApiMethod<Message>> handle(Message message) {
        Long chatId = message.getChatId();

        SendMessage sendMessage = replyMessageService.getTextMessage(chatId, "Здесь вы можете добавить фильтры для поиска");

        //TODO подумать над фильтрами
        ReplyKeyboard replyKeyboard = InlineKeyboardMarkupBuilder.create(chatId)
                .row()
                .button("Радиус", "/location_search")
                .button("Рейтинг", "/text_search")
                .endRow()
                .build();
        return null;
    }
}
