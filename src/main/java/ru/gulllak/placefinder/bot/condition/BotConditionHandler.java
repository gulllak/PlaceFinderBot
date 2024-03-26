package ru.gulllak.placefinder.bot.condition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gulllak.placefinder.bot.BotCondition;
import ru.gulllak.placefinder.bot.handler.MessageHandler;
import ru.gulllak.placefinder.exception.NoHandlerFoundException;
import ru.gulllak.placefinder.service.ReplyMessageService;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotConditionHandler {
    private final List<MessageHandler> messageHandlers;

    private final ReplyMessageService replyMessageService;

    public List<PartialBotApiMethod<? extends Serializable>> handleTextMessageByCondition(Message message, BotCondition botCondition) {
        MessageHandler messageHandler;
        try {
            messageHandler = messageHandlers.stream()
                    .filter(m -> m.canHandle(botCondition))
                    .findAny()
                    .orElseThrow(NoHandlerFoundException::new);
        } catch (NoHandlerFoundException ex) {
            return Collections.singletonList(replyMessageService.getTextMessage(message.getChatId(), "Невозможно обработать запрос"));
        }
        return messageHandler.handle(message);
    }
}
