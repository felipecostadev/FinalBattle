package br.com.felipecostadev.prototype.client.scoreboard;

import br.com.felipecostadev.prototype.FinalBattle;
import br.com.felipecostadev.prototype.client.mode.ClientMode;
import br.com.felipecostadev.prototype.manager.Manager;
import br.com.felipecostadev.prototype.api.scoreboard.wrapper.ScoreboardWrapper;
import br.com.felipecostadev.prototype.client.Client;
import br.com.felipecostadev.prototype.game.stage.GameStage;
import org.bukkit.Bukkit;

public class Client2Scoreboard extends ScoreboardWrapper {

    private final Manager manager;

    private final Client client;

    public Client2Scoreboard(Client client) {
        super(client.getPlayer().getScoreboard());

        this.manager = FinalBattle.getManager();
        this.client = client;

        builder().setTitle("§e§lARENA: PVP");
    }

    @Override
    public void update() {
        int slot = 15;

        builder().setRow(slot--, "");

        if (manager.hasGameStage(GameStage.WAITING) || manager.hasGameStage(GameStage.STARTING)) {
            builder().setRow(slot--, "Inicia em: §7" + manager.getGameTimerFormatted());
            builder().setRow(slot--, "Players online: §7" + manager.getClientManager().getModeList(ClientMode.ALIVE).size() + "/" + Bukkit.getMaxPlayers());
        } else if (manager.hasGameStage(GameStage.INVINCIBILITY)) {
            builder().setRow(slot--, "Invencibilidade: §7" + manager.getGameTimerFormatted());
            builder().setRow(slot--, "Players online: §7" + manager.getClientManager().getModeList(ClientMode.ALIVE).size() + "/" + Bukkit.getMaxPlayers());
        } else {
            builder().setRow(slot--, "Tempo: §7" + manager.getGameTimerFormatted());
            builder().setRow(slot--, "Players online: §7" + manager.getClientManager().getModeList(ClientMode.ALIVE).size() + "/" + Bukkit.getMaxPlayers());
            builder().setRow(slot--, "");
            builder().setRow(slot--, "Kills: §6" + client.getKills());
        }
        builder().setRow(slot--, "");
        builder().setRow(slot--, "§agithub.com/felipecostadev");
    }
}