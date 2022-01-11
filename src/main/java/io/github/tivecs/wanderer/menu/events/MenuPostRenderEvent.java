package io.github.tivecs.wanderer.menu.events;

import io.github.tivecs.wanderer.menu.MenuObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MenuPostRenderEvent extends Event {

    public static HandlerList HANDLER_LIST = new HandlerList();

    private MenuObject menuObject;

    public MenuPostRenderEvent(MenuObject menuObject){
        this.menuObject = menuObject;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public MenuObject getMenuObject() {
        return menuObject;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
