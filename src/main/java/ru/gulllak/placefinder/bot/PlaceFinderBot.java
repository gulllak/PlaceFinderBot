package ru.gulllak.placefinder.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;

@Component
@Slf4j
public class PlaceFinderBot extends TelegramLongPollingBot {
    private final UpdateReceiver updateReceiver;

    public PlaceFinderBot(@Value("${bot.token}")String botToken, UpdateReceiver updateReceiver) {
        super(botToken);
        this.updateReceiver = updateReceiver;
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<BotApiMethod<Message>> responseToUser = updateReceiver.handleUpdate(update);

        for (PartialBotApiMethod<? extends Serializable> partialBotApiMethod : responseToUser) {
            if(partialBotApiMethod instanceof SendMessage) {
                try {
                    execute((SendMessage) partialBotApiMethod);

                } catch (TelegramApiException e) {
                    log.error("Error occurred while sending message to user: {}", e.getMessage());
                }
            }

            if(partialBotApiMethod instanceof SendPhoto) {
                try {
                    execute((SendPhoto) partialBotApiMethod);
                } catch (TelegramApiException e) {
                    log.error("Error occurred while sending message to user: {}", e.getMessage());
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "PlacesFinder_bot";
    }
}
