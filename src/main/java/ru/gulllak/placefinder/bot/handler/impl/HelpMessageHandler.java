package ru.gulllak.placefinder.bot.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gulllak.placefinder.bot.BotCondition;
import ru.gulllak.placefinder.bot.handler.MessageHandler;
import ru.gulllak.placefinder.service.ReplyMessageService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpMessageHandler implements MessageHandler {
    private final ReplyMessageService replyMessageService;

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.HELP);
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Message message) {
        Long chatId = message.getChatId();

        SendMessage sendMessage = replyMessageService.getTextMessageHtml(chatId,
                String.join("\n",
                        "С помощью бота вы можете найти самые популярные места рядом с заданной локацией.\n",
                        "<b>Способы поиска мест:</b>",
                        "– Поделиться своей геопозицией с ботом, и поиск выполниться по вашим текущим координатам.",
                        "– Прикрепить геопозицию вручную, указав абсолютно любое место. (даже другой город)\n",
                        "<b><u>Как это работает?</u></b>",
                        "1. Поделитесь своей локацией: Начните с отправки вашего текущего местоположения боту.",
                        "2. Вам будут предложены фильтры поиска: расстояние и рейтинг.",
                        "3. После укажите, что вы хотите найти — достопримечательности для осмотра или места где можно вкусно покушать",
                        "4. Получите рекомендации: В ответ бот предоставит список мест отсортированных по убыванию количества отзывов, с кратким описанием, фотографией, оценкой пользователей, ссылкой на Google Maps",
                        "5. Выбрав интересующее место, вы можете изучить более подробную информацию перейдя по ссылке.\n",
                        "Начать поиск <a href=\"/start\">/start</a>"));

        return new ArrayList<>(Arrays.asList(sendMessage));
    }
}
