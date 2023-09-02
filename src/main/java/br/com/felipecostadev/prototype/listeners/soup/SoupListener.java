package br.com.felipecostadev.prototype.listeners.soup;

import br.com.felipecostadev.prototype.manager.Manager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupListener implements Listener {

    public SoupListener(Manager manager) {}

    @EventHandler
    public void onSoup(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();

        if (itemStack == null || !itemStack.getType().equals(Material.MUSHROOM_SOUP))
            return;

        Player player = event.getPlayer();

        boolean bowl = false;

        if (player.getHealth() < player.getMaxHealth()) {
            double health = player.getHealth() + 7;

            if (health > player.getMaxHealth())
                health = player.getMaxHealth();

            player.setHealth(health);

            bowl = true;
        }
        if (bowl) {
            player.setItemInHand(new ItemStack(Material.BOWL));
            player.updateInventory();
        }
        event.setCancelled(bowl);
    }
}