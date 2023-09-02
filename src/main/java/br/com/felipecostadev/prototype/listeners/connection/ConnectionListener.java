package br.com.felipecostadev.prototype.listeners.connection;

import br.com.felipecostadev.prototype.client.Client;
import br.com.felipecostadev.prototype.client.scoreboard.Client2Scoreboard;
import br.com.felipecostadev.prototype.game.stage.GameStage;
import br.com.felipecostadev.prototype.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private Manager manager;

    public ConnectionListener(Manager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (!manager.hasGameStage(GameStage.WAITING) && !manager.hasGameStage(GameStage.STARTING)) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cO jogo já iniciou.");
            return;
        }
        Client client = new Client(event.getPlayer());
        manager.getClientManager().put(client);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(-1000);
        player.setGameMode(GameMode.ADVENTURE);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.updateInventory();
        player.teleport(manager.getSpawnLocation());
        player.sendMessage("§aVocê entrou em: Arena PvP.");

        Client client = manager.getClientManager().get(player.getUniqueId());
        client.setScoreboard(new Client2Scoreboard(client));
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        manager.getClientManager().remove(event.getPlayer().getUniqueId());
    }
}