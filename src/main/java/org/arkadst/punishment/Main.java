package org.arkadst.punishment;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        if (!(new File(this.getDataFolder(), "config.yml")).exists()) {
            saveDefaultConfig();
        }
        config = getConfig();

        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getCommand("punish").setExecutor(new PunishCommand());
        getCommand("punishment").setExecutor(new PunishmentCommand(this));
    }

    @Override
    public void onDisable() {

    }
}
