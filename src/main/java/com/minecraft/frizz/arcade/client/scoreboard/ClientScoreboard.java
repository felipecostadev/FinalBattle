package com.minecraft.frizz.arcade.client.scoreboard;

import com.minecraft.frizz.arcade.Arcade;
import com.minecraft.frizz.arcade.api.scoreboard.ScoreboardBuilder;
import com.minecraft.frizz.arcade.client.Client;
import com.minecraft.frizz.arcade.client.mode.ClientMode;
import com.minecraft.frizz.arcade.game.stage.GameStage;
import com.minecraft.frizz.arcade.manager.Manager;
import org.bukkit.Bukkit;

public class ClientScoreboard extends ScoreboardBuilder {

    private Manager manager;
    private Client client;

    public ClientScoreboard(Client client) {
        super(client.getPlayer());

        this.manager = Arcade.getManager();
        this.client = client;

        setTitle("§5§lARENA PVP");
        update();
    }

    @Override
    public void update() {
        String[] pregame = new String[] { "", "Iniciando em: §7" + manager.getGameTimerFormatted(), "Jogadores: §7" + manager.getClientManager().getModeList(ClientMode.ALIVE).size() + "/" + Bukkit.getMaxPlayers(), "", "Sala: §e#1", "", "§ewww.mc-stew.com.br" };
        String[] invincibility = new String[] { "", "Invencível por: §7" + manager.getGameTimerFormatted(), "Jogadores: §7" + manager.getClientManager().getModeList(ClientMode.ALIVE).size() + "/" + Bukkit.getMaxPlayers(), "", "Sala: §e#1", "", "§ewww.mc-stew.com.br" };
        String[] progress = new String[] { "", "Tempo de jogo: §7" + manager.getGameTimerFormatted(), "Jogadores: §7" + manager.getClientManager().getModeList(ClientMode.ALIVE).size() + "/" + Bukkit.getMaxPlayers(), "Kills: §e" + client.getKills(), "", "Sala: §e#1", "", "§ewww.mc-stew.com.br" };

        setRow((manager.hasGameStage(GameStage.WAITING) || manager.hasGameStage(GameStage.STARTING) ? pregame : (manager.hasGameStage(GameStage.INVINCIBILITY) ? invincibility : progress)));
    }
}