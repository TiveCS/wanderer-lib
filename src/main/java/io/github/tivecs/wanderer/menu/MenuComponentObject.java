package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.language.Placeholder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MenuComponentObject {

    private final String componentId;
    private final Placeholder placeholder = new Placeholder();
    private final HashMap<String, Object> states = new HashMap<>();
    private final HashMap<String, Object> props = new HashMap<>();

    private ItemStack baseItem = null, currentItem = null;

    public MenuComponentObject(String componentId){
        this.componentId = componentId;
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

    public String getComponentId() {
        return componentId;
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

    public Placeholder getPlaceholder() {
        return placeholder;
    }

    public ItemStack getCurrentItem() {
        return currentItem;
    }
}
