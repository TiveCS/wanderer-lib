package io.github.tivecs.wanderer.menu.events;

import io.github.tivecs.wanderer.menu.MenuObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class MenuPreRenderEvent extends Event {

    public static HandlerList HANDLER_LIST = new HandlerList();

    private MenuObject menuObject;
    private HashMap<String, Object> props;

    public MenuPreRenderEvent(@Nonnull MenuObject menuObject, @Nullable HashMap<String, Object> props){
        this.menuObject = menuObject;
        this.props = props;
    }

    public MenuObject getMenuObject() {
        return menuObject;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public HashMap<String, Object> getProps() {
        return props;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
