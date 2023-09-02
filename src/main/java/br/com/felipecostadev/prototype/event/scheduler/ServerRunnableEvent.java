package br.com.felipecostadev.prototype.event.scheduler;

import br.com.felipecostadev.prototype.event.CustomEvent;

public class ServerRunnableEvent extends CustomEvent {

        /*
    second = value * 20
    minute = value * 1200
    hour = value * 72000
     */

    private int currentTick;

    public ServerRunnableEvent(int currentTick) {
        this.currentTick = currentTick;
    }

    public boolean isPeriodic(int tick) {
        return currentTick % tick == 0;
    }
}