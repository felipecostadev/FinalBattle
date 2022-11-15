package com.minecraft.frizz.arcade.listeners;

import com.minecraft.frizz.arcade.client.Client;
import com.minecraft.frizz.arcade.client.mode.ClientMode;
import com.minecraft.frizz.arcade.game.stage.GameStage;
import com.minecraft.frizz.arcade.manager.Manager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class ArcadeListener implements Listener {

    private Manager manager;

    public ArcadeListener(Manager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Client client = manager.getClientManager().get(event.getEntity().getUniqueId());

        if (!manager.hasGameStage(GameStage.PROGRESS) && !client.hasMode(ClientMode.SPECTATOR))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Client client = manager.getClientManager().get(event.getEntity().getUniqueId());

        if (!manager.hasGameStage(GameStage.PROGRESS) && !client.hasMode(ClientMode.SPECTATOR)) {
            event.setCancelled(true);
            return;
        }
        if (!(event.getDamager() instanceof Player))
            return;

        Player player = (Player) event.getDamager();
        ItemStack itemStack = player.getItemInHand();
        double damage = event.getDamage(), swordDamage = itemStack.getType().equals(Material.IRON_SWORD) ? 5.0 : 6.0;
        boolean isMore = false;
        
        if (damage > 1)
            isMore = true;

        damage = damage - 1.25 * itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
        damage += 1 * itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL);

        if (player.getFallDistance() > 0 && !player.isOnGround() && !player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            damage = damage - (swordDamage / 2);
            damage += 1;
        }
        if (isMore)
            damage -= 2;
        
        event.setDamage(damage);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        event.setBuild(false);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }
}