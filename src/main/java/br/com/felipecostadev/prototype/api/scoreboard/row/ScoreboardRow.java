package br.com.felipecostadev.prototype.api.scoreboard.row;

import org.bukkit.ChatColor;

import java.util.Arrays;

public enum ScoreboardRow {

    ROW_0,
    ROW_1,
    ROW_2,
    ROW_3,
    ROW_4,
    ROW_5,
    ROW_6,
    ROW_7,
    ROW_8,
    ROW_9,
    ROW_10,
    ROW_11,
    ROW_12,
    ROW_13,
    ROW_14,
    ROW_15,
    ROW_16;

    public String getEntry() {
        return ChatColor.values()[ordinal()] + ChatColor.RESET.toString();
    }

    public static ScoreboardRow findByOrdinal(int index) {
        return Arrays.stream(values())
                .filter(scoreboardRow -> scoreboardRow.ordinal() == index)
                .findFirst()
                .orElse(null);
    }
}