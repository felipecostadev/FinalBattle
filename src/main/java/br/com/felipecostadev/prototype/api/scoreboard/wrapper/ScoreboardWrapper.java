package br.com.felipecostadev.prototype.api.scoreboard.wrapper;

import br.com.felipecostadev.prototype.api.scoreboard.ScoreboardCustom;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ScoreboardWrapper {

    private final Scoreboard scoreboard;

    private final ScoreboardCustom scoreboardCustom;

    public ScoreboardWrapper(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
        this.scoreboardCustom = new ScoreboardCustom(this, getObjective(DisplaySlot.SIDEBAR));
    }

    public abstract void update();

    public Scoreboard getInterface() {
        return scoreboard;
    }

    public ScoreboardCustom builder() {
        return scoreboardCustom;
    }

    public Team getTeam(String name) {
        return getTeam(name, null);
    }

    public Team getTeam(String name, Consumer<Team> newTeam) {
        Team team = scoreboard.getTeam(name);

        if (team == null) {
            team = scoreboard.registerNewTeam(name);

            if (newTeam != null)
                newTeam.accept(team);
        }
        return team;
    }

    public Objective getObjective(DisplaySlot displaySlot) {
        return getObjective(displaySlot, null);
    }

    public Objective getObjective(DisplaySlot displaySlot, Consumer<Objective> newObjective) {
        Objective objective = scoreboard.getObjective(displaySlot.name());

        if (objective == null) {
            objective = scoreboard.registerNewObjective(displaySlot.name(), "dummy");
            objective.setDisplaySlot(displaySlot);

            if (newObjective != null)
                newObjective.accept(objective);
        }
        return objective;
    }

    public void removePlayerFromTeam(Player player) {
        scoreboard.getTeams().forEach(team -> {
            team.removeEntry(player.getName());

            if (team.getEntries().isEmpty())
                team.unregister();
        });
    }

    public void removePlayerFromTeam(Player player, String teamName) {
        Team team = scoreboard.getTeam(teamName);

        if (team != null && team.hasEntry(player.getName())) {
            team.removeEntry(player.getName());

            if (team.getEntries().isEmpty())
                team.unregister();
        }
    }

    public void destroyTeam(Predicate<Team> predicate) {
        scoreboard.getTeams().stream()
                .filter(predicate::test)
                .forEach(Team::unregister);
    }

    public void unregisterObjective(DisplaySlot displaySlot) {
        Objective objective = scoreboard.getObjective(displaySlot);

        if (objective != null)
            objective.unregister();
    }
}