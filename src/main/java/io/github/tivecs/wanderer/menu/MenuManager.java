package io.github.tivecs.wanderer.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.UUID;

public class MenuManager implements Listener {

    private final JavaPlugin plugin;
    private final HashMap<String, Menu> menus = new HashMap<>();
    private final HashMap<UUID, MenuObject> playerMenu = new HashMap<>();

    public MenuManager(@Nonnull JavaPlugin plugin){
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void registerMenu(Menu... menus){
        for (Menu m : menus){
            getMenus().put(m.getId(), m);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        UUID user = event.getWhoClicked().getUniqueId();
        MenuObject mo = getPlayerMenu().get(user);
        if (mo != null) mo.getMenu().onMenuClick(mo, event);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event){
        UUID user = event.getPlayer().getUniqueId();
        MenuObject mo = getPlayerMenu().get(user);
        if (mo != null) mo.getMenu().onMenuOpen(mo, event);
    }

    @EventHandler
    public void onInventoryOpen(InventoryCloseEvent event){
        UUID user = event.getPlayer().getUniqueId();
        MenuObject mo = getPlayerMenu().get(user);
        if (mo != null) mo.getMenu().onMenuClose(mo, event);
    }



    public void open(Player player, String menuId, int page, HashMap<String, Object> props){
        Menu m = findMenu(menuId);
        if (m != null){
            MenuObject mo = m.toObject(props, page);
            player.openInventory(mo.getInventory());
        }
    }

    public Menu findMenu(String menuId){
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
}
