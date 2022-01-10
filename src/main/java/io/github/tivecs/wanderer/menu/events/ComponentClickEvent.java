package io.github.tivecs.wanderer.menu.events;

import io.github.tivecs.wanderer.menu.MenuComponent;
import io.github.tivecs.wanderer.menu.MenuComponentObject;
import io.github.tivecs.wanderer.menu.MenuObject;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nonnull;

public class ComponentClickEvent extends Event implements Cancellable {

    public static HandlerList HANDLER_LIST = new HandlerList();

    private boolean isCancelled;

    private final int slot;
    private final MenuObject menuObject;
    private final MenuComponent component;
    private final MenuComponentObject componentObject;
    private final InventoryClickEvent clickEvent;

    public ComponentClickEvent(@Nonnull MenuObject menuObject, @Nonnull MenuComponentObject componentObject, int slot, InventoryClickEvent clickEvent){
        this.menuObject = menuObject;
        this.isCancelled = false;
        this.slot = slot;
        this.componentObject = componentObject;
        this.component = menuObject.getMenu().findComponent(componentObject.getComponentId());
        this.clickEvent = clickEvent;
    }

    public InventoryClickEvent getInventoryClickEvent() {
        return clickEvent;
    }

    public int getSlot() {
        return slot;
    }

    public MenuComponent getComponent() {
        return component;
    }

    public MenuComponentObject getComponentObject() {
        return componentObject;
    }

    public MenuObject getMenuObject() {
        return menuObject;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
