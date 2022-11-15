package com.minecraft.frizz.arcade.listeners.soup;

import com.minecraft.frizz.arcade.manager.Manager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupListener implements Listener {

    private Manager manager;

    public SoupListener(Manager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onSoup(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR)
            return;
        if (item.getType() == Material.MUSHROOM_SOUP) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if ((player).getHealth() < (player).getMaxHealth() || player.getFoodLevel() < 20) {
                    int restores = 7;
                    event.setCancelled(true);
                    if (player.getHealth() < (player).getMaxHealth())
                        if ((player).getHealth() + restores <= (player).getMaxHealth())
                            player.setHealth((player).getHealth() + restores);
                        else
                            player.setHealth((player).getMaxHealth());
                    else if (player.getFoodLevel() < 20)
                        if (player.getFoodLevel() + restores <= 20) {
                            player.setFoodLevel(player.getFoodLevel() + restores);
                            player.setSaturation(3);
                        } else {
                            player.setFoodLevel(20);
                            player.setSaturation(3);
                        }
                    item = new ItemStack(Material.BOWL);
                    player.setItemInHand(item);
                }
            }
        }
    }
}