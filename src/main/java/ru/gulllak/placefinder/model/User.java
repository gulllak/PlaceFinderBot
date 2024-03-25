package ru.gulllak.placefinder.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import ru.gulllak.placefinder.bot.BotCondition;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private Long userId;
    @Enumerated(EnumType.STRING)
    private BotCondition condition;
}
