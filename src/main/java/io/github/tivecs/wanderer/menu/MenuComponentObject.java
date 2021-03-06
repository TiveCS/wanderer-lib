package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.language.Placeholder;
import io.github.tivecs.wanderer.menu.events.ComponentCreationEvent;
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

    private int populationId;
    private int slot;
    private ItemStack baseItem = null, currentItem = null;

    public MenuComponentObject(@Nonnull MenuObject menuObject, @Nonnull MenuComponent component, int slot, int populationId){
        this.menuObject = menuObject;
        this.component = component;
        this.populationId = populationId;
        this.slot = slot;

        ComponentCreationEvent creationEvent = new ComponentCreationEvent(menuObject, component, this);
        Bukkit.getPluginManager().callEvent(creationEvent);
    }

    public void updateState(String key, Object value){
        ComponentStateUpdateEvent stateUpdateEvent = new ComponentStateUpdateEvent(this, key, getStates().get(key), value);

        getStates().put(key, value);
        getPlaceholder().set("state_" + key, value.toString());
        render();
        getMenuObject().visualizeSlot(getSlot());

        Bukkit.getPluginManager().callEvent(stateUpdateEvent);
    }

    public void render(){
        if (getBaseItem() != null){
            ItemStack currentItem = getBaseItem().clone();
            currentItem = getPlaceholder().useForItem(currentItem);
            setCurrentItem(currentItem);
        }
    }

    public void updatePlaceholder(){
        for (Map.Entry<String, Object> entry : getProps().entrySet()){
            getPlaceholder().set("props_" + entry.getKey(), entry.getValue().toString());
        }

        for (Map.Entry<String, Object> entry : getStates().entrySet()){
            getPlaceholder().set("state_" + entry.getKey(), entry.getValue().toString());
        }
    }

    public void setBaseItem(ItemStack baseItem) {
        this.baseItem = baseItem;
    }

    public void setCurrentItem(ItemStack currentItem) {
        this.currentItem = currentItem;
    }

    public void setPopulationId(int populationId) {
        this.populationId = populationId;
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

    public int getSlot() {
        return slot;
    }

    public int getPopulationId() {
        return populationId;
    }

    public ItemStack getCurrentItem() {
        return currentItem;
    }
}
