package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.menu.events.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.UUID;

public class MenuEventHandler implements Listener {

    private final MenuManager manager;

    public MenuEventHandler(MenuManager manager){
        this.manager = manager;
    }

    public void onMenuStateUpdate(MenuStateUpdateEvent event){
        event.getMenuObject().getMenu().onMenuStateUpdate(event);
    }

    public void onMenuPreRender(MenuPreRenderEvent event){
        event.getMenuObject().getMenu().onMenuPreRender(event);
    }

    public void onMenuPostRender(MenuPostRenderEvent event){
        event.getMenuObject().getMenu().onMenuPostRender(event);
    }

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

    public void onComponentStateUpdate(ComponentStateUpdateEvent event){
        MenuComponentInteraction interaction = event.getComponentObject().getComponent().getInteraction();
        if (interaction != null){
            interaction.onStateUpdate(event);
        }
    }

    public void onComponentCreation(ComponentCreationEvent event){
        MenuComponentInteraction interaction = event.getComponentObject().getComponent().getInteraction();
        if (interaction != null){
            interaction.onComponentCreation(event);
        }
    }

    public void onComponentClick(ComponentClickEvent event){
        if (!event.isCancelled()) {
            MenuComponentInteraction interaction = event.getComponent().getInteraction();
            if (interaction != null) {
                interaction.onClick(event);
            }
        }
    }

    public void onComponentPreRender(ComponentPreRenderEvent event){
        MenuComponentInteraction interaction = event.getComponent().getInteraction();
        if (interaction != null) {
            interaction.onPreRender(event);
        }
    }

    public void onComponentPostRender(ComponentPostRenderEvent event){
        MenuComponentInteraction interaction = event.getComponent().getInteraction();
        if (interaction != null) {
            interaction.onPostRender(event);
        }
    }

    public void onInventoryOpen(InventoryOpenEvent event){
        UUID user = event.getPlayer().getUniqueId();
        MenuObject mo = getManager().getPlayerMenu().get(user);
        if (mo != null) mo.getMenu().onMenuOpen(mo, event);
    }

    public void onInventoryOpen(InventoryCloseEvent event){
        UUID user = event.getPlayer().getUniqueId();
        MenuObject mo = getManager().getPlayerMenu().get(user);
        if (mo != null) mo.getMenu().onMenuClose(mo, event);
    }

    public MenuManager getManager() {
        return manager;
    }
}
