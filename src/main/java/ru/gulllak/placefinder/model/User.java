package ru.gulllak.placefinder.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;
import ru.gulllak.placefinder.bot.BotCondition;

@Entity
@Data
@Table(name = "users")
@ToString(exclude = "filter")
public class User {
    @Id
    @Column(name = "id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private BotCondition condition;

    @Column(name = "filter_state")
    private String filterState;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Filter filter;
}
