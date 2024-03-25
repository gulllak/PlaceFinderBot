package ru.gulllak.placefinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gulllak.placefinder.bot.BotCondition;
import ru.gulllak.placefinder.model.User;
import ru.gulllak.placefinder.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getById(long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public BotCondition getConditionByUserId(long userId) {
        User user = userRepository.findById(userId).orElse(null);
        BotCondition botCondition;

        if(user == null) {
            botCondition = BotCondition.MAIN_MENU;
        } else {
            botCondition = user.getCondition();
        }

        return botCondition;
    }
}
