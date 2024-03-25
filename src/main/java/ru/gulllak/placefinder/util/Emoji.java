package ru.gulllak.placefinder.util;

import com.vdurmont.emoji.EmojiParser;

public enum Emoji {
    HELLO(EmojiParser.parseToUnicode(":wave:")),
    ATTRACTION(EmojiParser.parseToUnicode(":japanese_castle:")),
    PIZZA(EmojiParser.parseToUnicode(":pizza:")),
    WORLD(EmojiParser.parseToUnicode(":earth_asia:")),
    LOCATION(EmojiParser.parseToUnicode(":round_pushpin:")),
    DOWN(EmojiParser.parseToUnicode(":arrow_down_small:"));

    private final String emojiName;

    Emoji(String emojiName) {
        this.emojiName = emojiName;
    }

    @Override
    public String toString() {
        return emojiName;
    }
}
