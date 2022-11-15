package com.minecraft.frizz.arcade.listeners.death;

import com.minecraft.frizz.arcade.client.Client;
import com.minecraft.frizz.arcade.client.mode.ClientMode;
import com.minecraft.frizz.arcade.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

public class DeathListener implements Listener {

    private Manager manager;

    public DeathListener(Manager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        Player victim = event.getEntity();
        victim.setHealth(20);
        victim.setVelocity(new Vector());
        victim.setAllowFlight(true);
        victim.setFlying(true);
        ((CraftPlayer) victim).getHandle().updateAbilities();
        victim.getActivePotionEffects().forEach(potionEffect -> victim.removePotionEffect(potionEffect.getType()));
        victim.getLocation().getWorld().strikeLightningEffect(victim.getLocation());
        victim.getInventory().clear();
        victim.getInventory().setArmorContents(null);
        victim.updateInventory();
        victim.teleport(victim.getLocation().add(0, 3, 0));

        Bukkit.getOnlinePlayers().forEach(player -> player.hidePlayer(victim));
        manager.getClientManager().getModeList(ClientMode.SPECTATOR).forEach(client -> client.getPlayer().showPlayer(victim));

        if (!manager.isDropItems())
            event.getDrops().clear();

        Client victimClient = manager.getClientManager().get(victim.getUniqueId());
        victimClient.setMode(ClientMode.SPECTATOR);

        Player killer = event.getEntity().getKiller();

        Client killerClient = manager.getClientManager().get(killer.getUniqueId());
        killerClient.setKills(killerClient.getKills() + 1);

        Bukkit.broadcastMessage("§b" + killer.getName() + " matou " + victim.getName() + " usando uma Espada.");
        Bukkit.broadcastMessage("§c" + manager.getClientManager().getModeList(ClientMode.ALIVE).size() + " jogadores restantes.");
    }
}