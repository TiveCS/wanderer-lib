package io.github.tivecs.wanderer.menu.events;

import io.github.tivecs.wanderer.menu.MenuObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MenuStateUpdateEvent extends Event {

    public static HandlerList HANDLER_LIST = new HandlerList();

    private final MenuObject menuObject;
    private final String key;
    private final Object previousValue;
    private final Object value;

    public MenuStateUpdateEvent(MenuObject menuObject, String key, Object previousValue, Object value){
        this.menuObject = menuObject;
        this.key = key;
        this.previousValue = previousValue;
        this.value = value;
    }

    public MenuObject getMenuObject() {
        return menuObject;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Object getPreviousValue() {
        return previousValue;
    }

    public Object getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
