package br.com.felipecostadev.prototype.api.scoreboard;

import br.com.felipecostadev.prototype.api.scoreboard.wrapper.ScoreboardWrapper;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardCustom {

    private final ScoreboardWrapper scoreboardWrapper;

    private final Objective objective;

    private final Map<Integer, Row> rowMap = new ConcurrentHashMap<>();

    public ScoreboardCustom(ScoreboardWrapper scoreboardWrapper, Objective objective) {
        this.scoreboardWrapper = scoreboardWrapper;
        this.objective = objective;
    }

    public boolean isInternalUsageTeam(Team team) {
        return team != null && team.getName().startsWith("sb_row:");
    }

    public void setTitle(String argument) {
        if (argument == null) {
            throw new NullPointerException("Argument cannot be null");
        } else if (argument.length() > 32) {
            throw new IllegalArgumentException("Argument too big");
        }
        objective.setDisplayName(argument);
    }

    public void setRow(int slot, String argument) {
        validateSlot(slot);

        if (argument == null)
            throw new NullPointerException("Argument cannot be null");

        Row row = rowMap.computeIfAbsent(slot, v -> createRow(slot));

        String prefix = argument;
        String suffix = "";

        if (argument.length() > 16) {
            int substring = (argument.charAt(15) == ChatColor.COLOR_CHAR) ? 15 : 16;

            prefix = argument.substring(0, substring);

            String lastColors = ChatColor.getLastColors(prefix);
            suffix = lastColors + argument.substring(substring);

            if (suffix.length() > 16)
                suffix = suffix.substring(0, 16);
        }
        row.setPrefix(prefix);
        row.setSuffix(suffix);
    }

    public void removeRow(int slot) {
        Row row = rowMap.remove(slot);

        if (row != null)
            row.destroy();
    }

    public void removeRows() {
        if (rowMap.isEmpty())
            return;

        rowMap.values().forEach(Row::destroy);
        rowMap.clear();
    }

    private Row createRow(int slot) {
        String score = ChatColor.values()[slot - 1] + "" + ChatColor.RESET;

        objective.getScore(score).setScore(slot);

        Team team = scoreboardWrapper.getTeam("sb_row:" + slot, customTeam -> customTeam.addEntry(score));

        return new Row(score, team);
    }

    private void validateSlot(int slot) {
        if (slot < 1 || slot > 15)
            throw new IllegalArgumentException("Scoreboard Row slot \"" + slot + "\" is invalid!");
    }

    static class Row {

        private final String score;

        private final Team team;

        public Row(String score, Team team) {
            this.score = score;
            this.team = team;
        }

        public String getScore() {
            return score;
        }

        public Team getTeam() {
            return team;
        }

        public void destroy() {
            team.getScoreboard().resetScores(score);
            team.unregister();
        }

        public void setPrefix(String prefix) {
            team.setPrefix(prefix);
        }

        public void setSuffix(String suffix) {
            team.setSuffix(suffix);
        }
    }
}