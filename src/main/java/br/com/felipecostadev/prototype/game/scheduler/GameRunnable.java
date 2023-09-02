package br.com.felipecostadev.prototype.game.scheduler;

import br.com.felipecostadev.prototype.api.item.ItemBuilder;
import br.com.felipecostadev.prototype.client.Client;
import br.com.felipecostadev.prototype.client.mode.ClientMode;
import br.com.felipecostadev.prototype.manager.Manager;
import br.com.felipecostadev.prototype.game.stage.GameStage;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class GameRunnable extends BukkitRunnable {

    private final Manager manager;

    public GameRunnable(Manager manager) {
        this.manager = manager;

        runTaskTimer(manager.getPlugin(), 0L, 20L);
    }

    @Override
    public void run() {
        List<Client> clientAliveList = manager.getClientManager().getModeList(ClientMode.ALIVE);

        if (manager.hasGameStage(GameStage.WAITING)) {
            if (clientAliveList.size() < 2)
                return;

            manager.setGameStage(GameStage.STARTING);
        } else if (manager.hasGameStage(GameStage.STARTING)) {
            if (clientAliveList.size() < 2) {
                manager.setGameTimer(90);
                manager.setGameStage(GameStage.WAITING);
                return;
            }
            if (manager.getGameTimer() >= 60 && manager.getGameTimer() % 60 == 0 || manager.getGameTimer() == 30 || manager.getGameTimer() == 15 || manager.getGameTimer() == 10 || manager.getGameTimer() < 6 && manager.getGameTimer() > 0) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                    player.sendMessage("§aA partida iniciará em §7" + (manager.getGameTimer() >= 60 ? ((manager.getGameTimer() / 60) % 60) : manager.getGameTimer()) + (manager.getGameTimer() > 1 && manager.getGameTimer() < 60 ? " segundos§a." : (manager.getGameTimer() > 60 ? " minutos§a." : (manager.getGameTimer() == 60 ? " minuto§a." : " segundo§a."))));
                });
            } else if (manager.getGameTimer() == 0) {
                manager.setGameTimer(10);
                manager.setGameStage(GameStage.INVINCIBILITY);

                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 1.0F, 1.0F);
                    player.sendMessage("§aA partida iniciou!");
                });
                clientAliveList.forEach(client -> manager.setInventory(client.getPlayer()));
                return;
            }
            manager.setGameTimer(manager.getGameTimer() - 1);

        } else if (manager.hasGameStage(GameStage.INVINCIBILITY)) {
            if (manager.getGameTimer() < 6 && manager.getGameTimer() > 0)
                Bukkit.broadcastMessage("§cA invencibilidade acabará em §7" + manager.getGameTimer() + (manager.getGameTimer() > 1 ? " segundos§c." : " segundo§c."));
            else if (manager.getGameTimer() == 0) {
                manager.setGameTimer(0);
                manager.setGameStage(GameStage.PROGRESS);

                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 1.0F, 1.0F);
                    player.sendMessage("§cA invencibilidade acabou!");
                });
                return;
            }
            manager.setGameTimer(manager.getGameTimer() - 1);

        } else if (manager.hasGameStage(GameStage.PROGRESS)) {
            if (clientAliveList.size() == 1) {
//                manager.setGameTimer(15);
//                manager.setGameStage(GameStage.ENDGAME);
//                return;
            }
            if (manager.getGameTimer() == 180) {
                clientAliveList.forEach(client -> {
                    PlayerInventory playerInventory = client.getPlayer().getInventory();
                    int slot = playerInventory.first(Material.IRON_SWORD);
                    playerInventory.setItem(slot, new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).build());
                });
                Bukkit.broadcastMessage("§e§lA partida chegou aos 3 minutos e todos receberam uma melhoria em suas espadas!");
            }
            if (manager.getGameTimer() == 480) {
                manager.setDropItems(true);
                clientAliveList.forEach(client -> {
                    PlayerInventory playerInventory = client.getPlayer().getInventory();
                    int slot = playerInventory.first(Material.DIAMOND_SWORD);
                    playerInventory.setItem(slot, new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).build());
                });
                Bukkit.broadcastMessage("§e§lA partida chegou aos 8 minutos e todos receberam uma melhoria em suas espadas!\n§bdrops §efoi alterado para §6true§e.");
            }
            if (manager.getGameTimer() == 780) {
                clientAliveList.forEach(client -> {
                    PlayerInventory playerInventory = client.getPlayer().getInventory();
                    int slot = playerInventory.first(Material.DIAMOND_SWORD);
                    playerInventory.setItem(slot, new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3).build());
                });
                Bukkit.broadcastMessage("§e§lA partida chegou aos 13 minutos e todos receberam uma melhoria em suas espadas!");
            }
            if (manager.getGameTimer() == 1080) {
                clientAliveList.forEach(client -> {
                    Player player = client.getPlayer();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1000000000, 2));

                    int slot = player.getInventory().first(Material.DIAMOND_SWORD);
                    player.getInventory().setItem(slot, new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).build());
                });
                Bukkit.broadcastMessage("§e§lA partida chegou aos 18 minutos e todos receberam o efeito de Decomposição III e a última melhoria em suas espadas!");
            }
            if (manager.isDropItems() && manager.getGameTimer() % 60 == 0) {
                for (Entity entity : manager.getSpawnLocation().getWorld().getEntities()) {
                    if (entity instanceof Item)
                        entity.remove();
                }
            }
            manager.setGameTimer(manager.getGameTimer() + 1);

        } else if (manager.hasGameStage(GameStage.ENDGAME)) {
            if (manager.getGameTimer() == 15) {
                Player player = clientAliveList.get(0).getPlayer();
                player.setGameMode(GameMode.SURVIVAL);
                player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.getInventory().setHeldItemSlot(0);
                player.getInventory().setItem(0, new ItemBuilder(Material.WATER_BUCKET).name("§aMLG").lore("§7Balde de Água").build());
                player.updateInventory();

                Location cakeLocation = player.getLocation().clone();
                cakeLocation.setY(cakeLocation.getY() + 20);

                for (int x = -2; x <= 2; ++x) {
                    for (int z = -2; z <= 2; ++z) {
                        cakeLocation.clone().add(x, 0, z).getBlock().setType(Material.GLASS);
                        cakeLocation.clone().add(x, 1, z).getBlock().setType(Material.CAKE_BLOCK);
                    }
                }
                player.teleport(cakeLocation.add(0, 1.5, 0));
            } else if (manager.getGameTimer() == 0) {
                Bukkit.shutdown();
                return;
            }
            manager.setGameTimer(manager.getGameTimer() - 1);
        }
    }
}