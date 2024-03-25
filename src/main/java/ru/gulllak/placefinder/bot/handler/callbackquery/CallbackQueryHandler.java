package ru.gulllak.placefinder.bot.handler.callbackquery;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface CallbackQueryHandler {
    List<BotApiMethod<Message>> handleCallbackQuery(CallbackQuery callbackQuery);
}
