package ru.gulllak.placefinder.bot.handler.callbackquery;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;
import java.util.List;

public interface CallbackQueryHandler {
    List<BotApiMethod<? extends Serializable>> handleCallbackQuery(CallbackQuery callbackQuery);
}
