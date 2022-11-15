package com.minecraft.frizz.arcade;

import com.minecraft.frizz.arcade.manager.Manager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Arcade extends JavaPlugin {

    private static Manager manager;

    public static Manager getManager() {
        return manager;
    }

    @Override
    public File getFile() {
        return super.getFile();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        manager = new Manager(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        manager.disable();
    }
}