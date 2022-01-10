package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.menu.events.ComponentClickEvent;
import io.github.tivecs.wanderer.menu.events.ComponentPostRenderEvent;
import io.github.tivecs.wanderer.menu.events.ComponentPreRenderEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.UUID;

public class MenuEventHandler implements Listener {

    private MenuManager manager;

    public MenuEventHandler(MenuManager manager){
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event){
        UUID user = event.getWhoClicked().getUniqueId();
        MenuObject mo = getManager().getPlayerMenu().get(user);
        if (mo != null && !event.isCancelled()) {
            int slot = event.getSlot();
            mo.getMenu().onMenuClick(mo, event);

            MenuComponentObject componentObject = mo.getComponentMap().get(slot);
            if (componentObject != null){
                ComponentClickEvent componentClickEvent = new ComponentClickEvent(mo, componentObject, slot, event);
                Bukkit.getPluginManager().callEvent(componentClickEvent);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onComponentClick(ComponentClickEvent event){
        if (!event.isCancelled()) {
            MenuComponentInteraction interaction = event.getComponent().getInteraction();
            if (interaction != null) {
                interaction.onClick(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onComponentPreRender(ComponentPreRenderEvent event){
        MenuComponentInteraction interaction = event.getComponent().getInteraction();
        if (interaction != null) {
            interaction.onPreRender(event);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onComponentPostRender(ComponentPostRenderEvent event){
        MenuComponentInteraction interaction = event.getComponent().getInteraction();
        if (interaction != null) {
            interaction.onPostRender(event);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryOpen(InventoryOpenEvent event){
        UUID user = event.getPlayer().getUniqueId();
        MenuObject mo = getManager().getPlayerMenu().get(user);
        if (mo != null) mo.getMenu().onMenuOpen(mo, event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryOpen(InventoryCloseEvent event){
        UUID user = event.getPlayer().getUniqueId();
        MenuObject mo = getManager().getPlayerMenu().get(user);
        if (mo != null) mo.getMenu().onMenuClose(mo, event);
    }

    public MenuManager getManager() {
        return manager;
    }
}
