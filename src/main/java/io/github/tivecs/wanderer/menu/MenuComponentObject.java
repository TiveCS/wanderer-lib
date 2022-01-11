package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.language.Placeholder;
import io.github.tivecs.wanderer.menu.events.ComponentStateUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class MenuComponentObject {

    private final MenuObject menuObject;
    private final MenuComponent component;
    private final Placeholder placeholder = new Placeholder();
    private final HashMap<String, Object> states = new HashMap<>();
    private final HashMap<String, Object> props = new HashMap<>();

    private ItemStack baseItem = null, currentItem = null;

    public MenuComponentObject(@Nonnull MenuObject menuObject, @Nonnull MenuComponent component){
        this.menuObject = menuObject;
        this.component = component;
    }

    public void updateState(String key, Object value){
        ComponentStateUpdateEvent stateUpdateEvent = new ComponentStateUpdateEvent(this, key, getStates().get(key), value);
        getStates().put(key, value);
        Bukkit.getPluginManager().callEvent(stateUpdateEvent);
    }

    public void render(){
        if (getBaseItem() != null){
            setCurrentItem(getPlaceholder().useForItem(getBaseItem().clone()));
        }
    }

    public void updatePlaceholder(){
        getPlaceholder().getPlaceholders().clear();
        for (Map.Entry<String, Object> entry : getProps().entrySet()){
            getPlaceholder().set("props_" + entry.getKey(), (String) entry.getValue());
        }

        for (Map.Entry<String, Object> entry : getProps().entrySet()){
            getPlaceholder().set("state_" + entry.getKey(), (String) entry.getValue());
        }
    }

    public void setBaseItem(ItemStack baseItem) {
        this.baseItem = baseItem;
    }

    public void setCurrentItem(ItemStack currentItem) {
        this.currentItem = currentItem;
    }

    public MenuComponent getComponent() {
        return component;
    }

    public HashMap<String, Object> getStates() {
        return states;
    }

    public HashMap<String, Object> getProps() {
        return props;
    }

    public ItemStack getBaseItem() {
        return baseItem;
    }

    public MenuObject getMenuObject() {
        return menuObject;
    }

    public Placeholder getPlaceholder() {
        return placeholder;
    }

    public ItemStack getCurrentItem() {
        return currentItem;
    }
}
