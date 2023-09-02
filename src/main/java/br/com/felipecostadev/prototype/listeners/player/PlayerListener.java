package br.com.felipecostadev.prototype.listeners.player;

import br.com.felipecostadev.prototype.event.scheduler.ServerRunnableEvent;
import br.com.felipecostadev.prototype.manager.Manager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerListener implements Listener {

    private Manager manager;

    public PlayerListener(Manager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onDropped(PlayerDropItemEvent event) {
        if (!manager.isDropItems())
            event.getItemDrop().remove();
    }


    @EventHandler
    public void onRunnable(ServerRunnableEvent event) {
        if (event.isPeriodic(20)) {
            manager.getClientManager().getCollection().stream()
                    .filter(client -> client.getScoreboard() != null)
                    .forEach(client -> client.getScoreboard().update());
        }
    }
}