package br.com.felipecostadev.prototype.client;

import br.com.felipecostadev.prototype.client.mode.ClientMode;
import br.com.felipecostadev.prototype.client.scoreboard.Client2Scoreboard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class Client {

    private final Player player;

    private ClientMode mode;

    private int kills;

    private Client2Scoreboard scoreboard;

    public Client(Player player) {
        this.player = player;
        this.mode = ClientMode.ALIVE;
    }

    public boolean hasMode(ClientMode clientMode) {
        return mode.equals(clientMode);
    }
}