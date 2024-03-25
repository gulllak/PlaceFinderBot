package ru.gulllak.placefinder.bot.handler.callbackquery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
@Slf4j
public class PlaceFinderCallbackQueryHandler implements CallbackQueryHandler {

    @Override
    public List<BotApiMethod<Message>> handleCallbackQuery(CallbackQuery callbackQuery) {
        return null;
    }
}
