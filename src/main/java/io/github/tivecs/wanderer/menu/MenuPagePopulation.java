package io.github.tivecs.wanderer.menu;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MenuPagePopulation {

    private final MenuObject menuObject;
    private final int page;

    private final HashMap<String, LinkedHashMap<Integer, MenuComponentObject>> componentPopulations = new HashMap<>();

    public MenuPagePopulation(MenuObject menuObject, int page){
        this.menuObject = menuObject;
        this.page = page;
    }

    public void reset(){
        for (LinkedHashMap<Integer, MenuComponentObject> components : getComponentPopulations().values()){
            components.clear();
        }
    }

    public void registerComponent(MenuComponentObject componentObject){
        String componentId = componentObject.getComponent().getComponentId();
        LinkedHashMap<Integer, MenuComponentObject> components = getComponentPopulations().getOrDefault(componentId, new LinkedHashMap<>());

        components.put(componentObject.getPopulationId(), componentObject);
        getComponentPopulations().put(componentId, components);
    }

    public void unregisterComponent(MenuComponentObject componentObject){
        String componentId = componentObject.getComponent().getComponentId();
        LinkedHashMap<Integer, MenuComponentObject> components = getComponents(componentId);
        if (components != null){
            components.remove(componentObject.getPopulationId());
        }
    }

    public LinkedHashMap<Integer, MenuComponentObject> getComponents(String componentId){
        return getComponentPopulations().get(componentId);
    }

    public MenuComponentObject getComponent(String componentId, int populationId){
        LinkedHashMap<Integer, MenuComponentObject> components = getComponents(componentId);
        if (components != null){
            return components.get(populationId);
        }
        return null;
    }

    public MenuObject getMenuObject() {
        return menuObject;
    }

    public int getPage() {
        return page;
    }

    public HashMap<String, LinkedHashMap<Integer, MenuComponentObject>> getComponentPopulations() {
        return componentPopulations;
    }
}
