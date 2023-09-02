package br.com.felipecostadev.prototype.scheduler;

import br.com.felipecostadev.prototype.FinalBattle;
import br.com.felipecostadev.prototype.event.scheduler.ServerRunnableEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerRunnable extends BukkitRunnable {

    private int currentTick;

    public ServerRunnable(FinalBattle plugin) {
        runTaskTimer(plugin, 1L, 1L);
    }

    @Override
    public void run() {
        Bukkit.getPluginManager().callEvent(new ServerRunnableEvent(++currentTick));
    }
}