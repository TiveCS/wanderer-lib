package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.language.Placeholder;
import io.github.tivecs.wanderer.menu.events.MenuPostRenderEvent;
import io.github.tivecs.wanderer.menu.events.MenuPreRenderEvent;
import io.github.tivecs.wanderer.menu.events.MenuStateUpdateEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Menu {

    private final String id;
    private final Placeholder placeholder = new Placeholder();
    private final List<String> mapping = new ArrayList<>();
    private final HashMap<String, Object> states = new HashMap<>();

    private final HashMap<String, MenuComponent> components = new HashMap<>();
    private final HashMap<Character, MenuComponent> componentsByMapId = new HashMap<>();
    private final HashMap<String, Integer> potentialComponentPopulation = new HashMap<>();

    private String title;

    public Menu(String id, String title){
        this.id = id;
        this.title = title;
    }

    public Menu(String id){
        this(id, null);
    }

    public abstract void onMenuClick(MenuObject menuObject, InventoryClickEvent event);
    public abstract void onMenuStateUpdate(MenuStateUpdateEvent event);
    public abstract void onMenuPreRender(MenuPreRenderEvent event);
    public abstract void onMenuPostRender(MenuPostRenderEvent event);
    public abstract void onMenuOpen(MenuObject menuObject, InventoryOpenEvent event);
    public abstract void onMenuClose(MenuObject menuObject, InventoryCloseEvent event);

    public Menu setState(String key, Object value){
        getStates().put(key, value);
        return this;
    }

    public void addMap(String map){
        if (map.length() == 9) {
            getMapping().add(map);
        }
    }

    public MenuComponent addComponent(String componentId, char mapId){
        MenuComponent component = new MenuComponent(componentId, mapId);
        getComponents().put(componentId, component);
        getComponentsByMapId().put(mapId, component);
        return component;
    }

    public MenuObject toObject(@Nullable MenuManager manager, @Nullable HashMap<String, Object> props, int page){
        MenuObject menuObject = new MenuObject(manager, this, props);
        menuObject.setPage(page);
        return menuObject;
    }

    public MenuObject toObject(@Nullable HashMap<String, Object> props, int page){
        return toObject(null, props, page);
    }

    public int calculatePotentialPopulation(String componentId, boolean recalculate){
        int potential = getPotentialComponentPopulation().getOrDefault(componentId, 0);
        if (recalculate || (potential == 0 && !getPotentialComponentPopulation().containsKey(componentId))){

            for (String map : getMapping()){
                char[] mapIds = map.toCharArray();
                for (char mapId : mapIds){
                    MenuComponent component = getComponentsByMapId().get(mapId);
                    if (component != null && component.getComponentId().equals(componentId)) potential++;
                }
            }

            getPotentialComponentPopulation().put(componentId, potential);
        }
        return potential;
    }

    public MenuObject toObject(HashMap<String, Object> props){
        return toObject(props,1);
    }

    public MenuObject toObject(int page){
        return toObject(null,page);
    }

    public MenuObject toObject(){
        return toObject(null, 1);
    }

    public MenuComponent findComponent(String componentId){
        return getComponents().get(componentId);
    }

    public MenuComponent findComponent(char mapId){
        return getComponentsByMapId().get(mapId);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Placeholder getPlaceholder() {
        return placeholder;
    }

    public List<String> getMapping() {
        return mapping;
    }

    public HashMap<Character, MenuComponent> getComponentsByMapId() {
        return componentsByMapId;
    }

    public HashMap<String, Object> getStates() {
        return states;
    }

    public HashMap<String, MenuComponent> getComponents() {
        return components;
    }

    public String getId() {
        return id;
    }

    protected HashMap<String, Integer> getPotentialComponentPopulation() {
        return potentialComponentPopulation;
    }
}
