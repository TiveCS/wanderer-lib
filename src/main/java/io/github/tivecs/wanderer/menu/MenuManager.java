package io.github.tivecs.wanderer.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class MenuManager{

    private final JavaPlugin plugin;
    private final MenuEventHandler eventHandler;
    private final HashMap<String, Menu> menus = new HashMap<>();
    private final HashMap<UUID, MenuObject> playerMenu = new HashMap<>();

    public MenuManager(@Nonnull JavaPlugin plugin){
        this.plugin = plugin;
        this.eventHandler = new MenuEventHandler(this);

       Bukkit.getPluginManager().registerEvents(this.eventHandler, plugin);
    }

    public void registerMenu(Menu... menus){
        for (Menu m : menus){
            getMenus().put(m.getId(), m);
        }
    }

    public void open(@Nonnull Player player, @Nonnull String menuId, int page, @Nullable HashMap<String, Object> props){
        Menu m = findMenu(menuId);
        if (m != null){
            MenuObject mo = m.toObject(props, page);
            player.openInventory(mo.getInventory());
        }
    }

    public Menu findMenu(@Nonnull String menuId){
        return getMenus().get(menuId);
    }

    public HashMap<String, Menu> getMenus() {
        return menus;
    }

    public HashMap<UUID, MenuObject> getPlayerMenu() {
        return playerMenu;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public MenuEventHandler getEventHandler() {
        return eventHandler;
    }
}
