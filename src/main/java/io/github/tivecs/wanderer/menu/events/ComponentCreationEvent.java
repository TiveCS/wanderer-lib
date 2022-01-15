package io.github.tivecs.wanderer.menu.events;

import io.github.tivecs.wanderer.menu.MenuComponent;
import io.github.tivecs.wanderer.menu.MenuComponentObject;
import io.github.tivecs.wanderer.menu.MenuObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class ComponentCreationEvent extends Event {

    public static HandlerList HANDLER_LIST = new HandlerList();

    private final MenuComponent component;
    private final MenuComponentObject componentObject;
    private final MenuObject menuObject;

    public ComponentCreationEvent(@Nonnull MenuObject menuObject,  @Nonnull MenuComponent component, @Nonnull MenuComponentObject componentObject){
        this.menuObject = menuObject;
        this.component = component;
        this.componentObject = componentObject;
    }

    public MenuObject getMenuObject() {
        return menuObject;
    }

    public MenuComponent getComponent() {
        return component;
    }

    public MenuComponentObject getComponentObject() {
        return componentObject;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
