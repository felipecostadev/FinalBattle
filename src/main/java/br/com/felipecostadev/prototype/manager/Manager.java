package br.com.felipecostadev.prototype.manager;

import br.com.felipecostadev.prototype.FinalBattle;
import br.com.felipecostadev.prototype.api.item.ItemBuilder;
import br.com.felipecostadev.prototype.manager.client.ClientManager;
import br.com.felipecostadev.prototype.game.scheduler.GameRunnable;
import br.com.felipecostadev.prototype.game.stage.GameStage;
import br.com.felipecostadev.prototype.scheduler.ServerRunnable;
import br.com.felipecostadev.prototype.utils.ClassGetter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class Manager {

    private final FinalBattle plugin;

    private final Location spawnLocation;
    private final ClientManager clientManager;

    private boolean dropItems;
    private int gameTimer;
    private GameStage gameStage;

    public Manager(FinalBattle plugin) {
        this.plugin = plugin;

        this.spawnLocation = new Location(Bukkit.getWorld("world"), 0.5, 100.0, 0.5);
        this.spawnLocation.getWorld().setAutoSave(false);
        plugin.getLogger().info("spawn location has been initialized.");

        this.clientManager = new ClientManager();
        plugin.getLogger().info("client manager services has been initialized.");

        this.dropItems = false;
        this.gameTimer = 90;
        this.gameStage = GameStage.WAITING;

        ClassGetter.loadPackageByFile(plugin.getFile(), "br.com.felipecostadev.prototype.listeners").stream()
                .filter(Listener.class::isAssignableFrom)
                .forEach(listenerClass -> {
                    try {
                        Listener listener = (Listener) listenerClass.getConstructor(Manager.class).newInstance(this);
                        Bukkit.getPluginManager().registerEvents(listener, plugin);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        new ServerRunnable(plugin);
        new GameRunnable(this);
    }

    public boolean hasGameStage(GameStage gameStage) {
        return this.gameStage.equals(gameStage);
    }

    public String getGameTimerFormatted() {
        int minutes = (gameTimer / 60) % 60, seconds = gameTimer % 60;
        return minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

    public void disable() {

    }

    public void setInventory(Player player) {
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

        player.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).build());
        player.getInventory().setItem(9, new ItemStack(Material.IRON_HELMET));
        player.getInventory().setItem(10, new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setItem(11, new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setItem(12, new ItemStack(Material.IRON_BOOTS));
        player.getInventory().setItem(13, new ItemBuilder(Material.BOWL, 64).build());
        player.getInventory().setItem(14, new ItemBuilder(Material.RED_MUSHROOM, 64).build());
        player.getInventory().setItem(15, new ItemBuilder(Material.BROWN_MUSHROOM, 64).build());
        player.getInventory().setItem(16, new ItemBuilder(Material.RED_MUSHROOM, 64).build());
        player.getInventory().setItem(22, new ItemBuilder(Material.BOWL, 64).build());
        player.getInventory().setItem(23, new ItemBuilder(Material.RED_MUSHROOM, 64).build());
        player.getInventory().setItem(24, new ItemBuilder(Material.BROWN_MUSHROOM, 64).build());
        player.getInventory().setItem(25, new ItemBuilder(Material.BROWN_MUSHROOM, 64).build());

        for (int index = 0; index <= 22; ++index)
            player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));

        player.getInventory().setHeldItemSlot(0);
        player.updateInventory();
    }
}