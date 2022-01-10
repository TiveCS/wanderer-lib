package io.github.tivecs.wanderer.menu.events;

import io.github.tivecs.wanderer.menu.MenuComponent;
import io.github.tivecs.wanderer.menu.MenuComponentObject;
import io.github.tivecs.wanderer.menu.MenuObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class ComponentPreRenderEvent extends Event {

    public static HandlerList HANDLER_LIST = new HandlerList();

    private final MenuObject menuObject;
    private final MenuComponent component;
    private final MenuComponentObject componentObject;
    private final HashMap<String, Object> props;

    public ComponentPreRenderEvent(@Nonnull MenuObject menuObject, @Nonnull MenuComponent component, @Nonnull MenuComponentObject componentObject, @Nullable HashMap<String, Object> props){
        this.menuObject = menuObject;
        this.component = component;
        this.componentObject = componentObject;
        this.props = props;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public MenuComponentObject getComponentObject() {
        return componentObject;
    }

    public MenuComponent getComponent() {
        return component;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public MenuObject getMenuObject() {
        return menuObject;
    }

    public HashMap<String, Object> getProps() {
        return props;
    }
}
