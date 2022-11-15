package com.minecraft.frizz.arcade.scheduler;

import com.minecraft.frizz.arcade.Arcade;
import com.minecraft.frizz.arcade.event.scheduler.ServerRunnableEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerRunnable extends BukkitRunnable {

    private int currentTick;

    public ServerRunnable(Arcade plugin) {
        runTaskTimer(plugin, 1L, 1L);
    }

    @Override
    public void run() {
        Bukkit.getPluginManager().callEvent(new ServerRunnableEvent(++currentTick));
    }
}