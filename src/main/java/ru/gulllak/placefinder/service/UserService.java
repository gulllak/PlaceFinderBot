package ru.gulllak.placefinder.service;

import ru.gulllak.placefinder.bot.BotCondition;
import ru.gulllak.placefinder.model.User;

public interface UserService {
    void save(User user);

    User getById(long userId);

    BotCondition getConditionByUserId(long userId);
}
