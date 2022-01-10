package io.github.tivecs.wanderer.menu.events;

import io.github.tivecs.wanderer.menu.MenuComponent;
import io.github.tivecs.wanderer.menu.MenuComponentObject;
import io.github.tivecs.wanderer.menu.MenuObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class ComponentPostRenderEvent extends Event {

    public static HandlerList HANDLER_LIST = new HandlerList();

    private final MenuObject menuObject;
    private final MenuComponent component;
    private final MenuComponentObject componentObject;

    public ComponentPostRenderEvent(@Nonnull MenuObject menuObject, @Nonnull MenuComponent component, @Nonnull MenuComponentObject componentObject){
        this.menuObject = menuObject;
        this.component = component;
        this.componentObject = componentObject;
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
}
