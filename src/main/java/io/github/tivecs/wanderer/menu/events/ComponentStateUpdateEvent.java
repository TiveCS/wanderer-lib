package io.github.tivecs.wanderer.menu.events;

import io.github.tivecs.wanderer.menu.MenuComponentObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ComponentStateUpdateEvent extends Event {

    public static HandlerList HANDLER_LIST = new HandlerList();

    private final MenuComponentObject componentObject;
    private final String key;
    private final Object previousValue;
    private final Object value;

    public ComponentStateUpdateEvent(MenuComponentObject componentObject, String key, Object previousValue, Object value){
        this.componentObject = componentObject;
        this.key = key;
        this.previousValue = previousValue;
        this.value = value;
    }

    public MenuComponentObject getComponentObject() {
        return componentObject;
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
