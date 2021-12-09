package io.github.tivecs.wanderer;

import io.github.tivecs.wanderer.cmd.CmdWanderer;
import io.github.tivecs.wanderer.storage.StorageManager;
import io.github.tivecs.wanderer.storage.StorageYML;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class WandererLib extends JavaPlugin {

    private static StorageManager storageManager = null;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        storageManager = new StorageManager();
        StorageYML config = new StorageYML(new File(getDataFolder(), "config.yml"));

        storageManager.registerStorage(config, true);

        getCommand("wanderer").setExecutor(new CmdWanderer());
    }

    public static StorageManager getStorageManager() {
        return storageManager;
    }
}
