package com.minecraft.frizz.arcade.client;

import com.minecraft.frizz.arcade.client.mode.ClientMode;
import com.minecraft.frizz.arcade.client.scoreboard.ClientScoreboard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class Client {

    private final Player player;

    private ClientMode mode;

    private int kills;

    private ClientScoreboard scoreboard;

    public Client(Player player) {
        this.player = player;
        this.mode = ClientMode.ALIVE;
    }

    public boolean hasMode(ClientMode clientMode) {
        return mode.equals(clientMode);
    }
}