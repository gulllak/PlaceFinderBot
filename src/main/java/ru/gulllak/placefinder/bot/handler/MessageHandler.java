package ru.gulllak.placefinder.bot.handler;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gulllak.placefinder.bot.BotCondition;

import java.io.Serializable;
import java.util.List;

public interface MessageHandler {
    boolean canHandle(BotCondition botCondition);

    List<PartialBotApiMethod<? extends Serializable>> handle(Message message);
}
