package ru.gulllak.placefinder.bot.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gulllak.placefinder.bot.BotCondition;

import java.util.List;

public interface MessageHandler {
    boolean canHandle(BotCondition botCondition);

    List<BotApiMethod<Message>> handle(Message message);
}
