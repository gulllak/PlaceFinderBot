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

import java.util.Collections;
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
    public List<BotApiMethod<Message>> handle(Message message) {
        Long chatId = message.getChatId();

        SendMessage sendMessage = replyMessageService.getTextMessage(chatId,
                String.join("\n\n",
                        "Привет " + message.getFrom().getFirstName() + "! " + Emoji.HELLO,
                        "Наш бот умеет искать наиболее интересные места рядом с тобой, а именно:",
                        Emoji.ATTRACTION + " Достопремечательности",
                        Emoji.PIZZA + " Рестораны\n",

                        Emoji.WORLD + " Поделитесть с нами своей геопозицией, или отправьте название города сообщением"));

        ReplyKeyboard replyKeyboard = InlineKeyboardMarkupBuilder.create(chatId)
                .setText("Вариант поиска")
                .row()
                .button("Поделиться моей локацией", "/location_search")
                .endRow()
                .row()
                .button("Ввести город", "/text_search")
                .endRow()
                .build();

        sendMessage.setReplyMarkup(replyKeyboard);


        return Collections.singletonList(sendMessage);
    }
}
