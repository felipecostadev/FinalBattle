package com.minecraft.frizz.arcade.api.scoreboard;

import com.google.common.collect.ImmutableList;
import com.minecraft.frizz.arcade.api.scoreboard.row.ScoreboardRow;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public abstract class ScoreboardBuilder {

    private Scoreboard scoreboard;
    private Objective objective;

    public ScoreboardBuilder(Player player) {
        this.scoreboard = player.getScoreboard();
        this.objective = scoreboard.registerNewObjective("info", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public abstract void update();

    public void setTitle(String displayName) {
        if (!objective.getDisplayName().equals(displayName))
            objective.setDisplayName(displayName);
    }

    public void setRow(String... arguments) {
        List<String> argumentList = new ArrayList<>();

        for (String argument : arguments) {
            if (!argument.isEmpty())
                argumentList.add(argument);
            else {
                ChatColor chatColor = ChatColor.values()[(int) (Math.random() * ChatColor.values().length)];
                argumentList.add(chatColor.toString());
            }
        }
        ImmutableList<String> argumentListReversed = ImmutableList.copyOf(argumentList).reverse();

        for (String argumentReversed : argumentListReversed)
            addRow(argumentListReversed.indexOf(argumentReversed), argumentReversed);
    }

    public void addRow(int index, String argument) {
        ScoreboardRow scoreboardRow = ScoreboardRow.findByOrdinal(index);

        if (argument.length() <= 16) {
            createTeam(scoreboardRow, argument, "");
        } else {
            if (argument.length() > 32)
                throw new ArrayIndexOutOfBoundsException("Row " + scoreboardRow.ordinal() + "'s argument exceeded character limit (" + argument.length() + " > 32).");

            int subString = (argument.charAt(15) == ChatColor.COLOR_CHAR) ? 15 : 16;

            String prefix = argument.substring(0, subString),
            lastColors = ChatColor.getLastColors(prefix),
            suffix = lastColors + argument.substring(subString);

            if (suffix.length() > 16)
                suffix = suffix.substring(0, 16);

            createTeam(scoreboardRow, prefix, suffix);
        }
    }

    public void removeRow(int index) {
        ScoreboardRow scoreboardRow = ScoreboardRow.findByOrdinal(index);

        scoreboard.resetScores(scoreboardRow.getEntry());

        Team team = scoreboard.getTeam(scoreboardRow.getEntry());

        if (team != null)
            team.unregister();
    }

    public void removeRows() {
        for (ScoreboardRow scoreboardRow : ScoreboardRow.values()) {
            scoreboard.resetScores(scoreboardRow.getEntry());

            Team team = scoreboard.getTeam(scoreboardRow.getEntry());

            if (team != null)
                team.unregister();
        }
    }

    private void createTeam(ScoreboardRow scoreboardRow, String prefix, String suffix) {
        String entry = scoreboardRow.getEntry();
        Team team = scoreboard.getTeam(entry);

        if (team == null)
            team = scoreboard.registerNewTeam(entry);

        if (!team.getPrefix().equals(prefix))
            team.setPrefix(prefix);

        if (!team.getPrefix().equals(suffix))
            team.setSuffix(suffix);

        if (!team.hasEntry(entry))
            team.addEntry(entry);

        Score score = objective.getScore(entry);
        score.setScore(scoreboardRow.ordinal());
    }
}