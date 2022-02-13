package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.language.Placeholder;
import io.github.tivecs.wanderer.menu.events.MenuPostRenderEvent;
import io.github.tivecs.wanderer.menu.events.MenuPreRenderEvent;
import io.github.tivecs.wanderer.menu.events.MenuStateUpdateEvent;
import io.github.tivecs.wanderer.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class MenuObject {

    private final MenuManager manager;
    private final Menu menu;
    private final Placeholder placeholder = new Placeholder();
    private final HashMap<String, Object> props;
    private final HashMap<String, Object> states = new HashMap<>();

    private Inventory inventory = null, previousInventory = null;
    private int row = 3;
    private int page = -1, previousPage = -1;
    private final HashMap<Integer, MenuComponentObject> componentMap = new HashMap<>();
    private final HashMap<String, Integer> componentPopulation = new HashMap<>();
    private final HashMap<Integer, MenuPagePopulation> pagePopulations = new HashMap<>();
    private final HashMap<String, Integer> potentialComponentPopulation = new HashMap<>();

    public MenuObject(@Nullable MenuManager manager, @Nonnull Menu menu, @Nullable HashMap<String, Object> props){
        this.manager = manager;
        this.menu = menu;
        this.props = props != null ? new HashMap<>(props) : null;

        int rowSize = menu.getMapping().size();
        if (rowSize > 0){
            this.row = rowSize;
        }
    }

    public void updateState(String key, Object value){
        MenuStateUpdateEvent stateUpdateEvent = new MenuStateUpdateEvent(this, key, getStates().get(key), value);
        StringBuilder keyPath = new StringBuilder("state_").append(key);

        getStates().put(keyPath.toString(), value);
        getPlaceholder().set(keyPath.toString(), value.toString());

        Bukkit.getPluginManager().callEvent(stateUpdateEvent);
    }

    public void render(){
        MenuPreRenderEvent preRenderEvent = new MenuPreRenderEvent(this, getProps());
        Bukkit.getPluginManager().callEvent(preRenderEvent);

        updateState("page", getPage());
        setInventory(prepareInventory());
        mappingComponent();
        visualizeMap();

        MenuPostRenderEvent postRenderEvent = new MenuPostRenderEvent(this);
        Bukkit.getPluginManager().callEvent(postRenderEvent);
    }

    public void visualizeMap(){
        getInventory().clear();
        for (Map.Entry<Integer, MenuComponentObject> entry : getComponentMap().entrySet()){
            getInventory().setItem(entry.getKey(), entry.getValue().getCurrentItem());
        }
    }

    public void visualizeSlot(int slot){
        getInventory().clear(slot);
        MenuComponentObject componentObject = getComponentMap().get(slot);
        if (componentObject != null){
            getInventory().setItem(slot, componentObject.getCurrentItem());
        }
    }

    public void mappingComponent(){
        getComponentMap().clear();
        getComponentPopulation().clear();
        if (getInventory() != null){
            for (int row = 0; row < getRow(); row++){
                String map = getMenu().getMapping().get(row);
                char[] mapIds = map.toCharArray();
                MenuPagePopulation pagePopulation = getPagePopulations().getOrDefault(getPage(), new MenuPagePopulation(this, getPage()));

                for (int column = 0; column < 9; column++){
                    int slot = (row*9) + column;
                    char mapId = mapIds[column];
                    MenuComponent component = getMenu().findComponent(mapId);

                    if (component != null){
                        String componentId = component.getComponentId();

                        // TODO Population Id on page > 1 behaviour is not as expected (5, 6, 7, ...). Occurred: 5, 8, 28, 114, ...
                        // TODO Change ComponentPopulation to ComponentPopulationPerPage (content: population id per component in that page)
                        LinkedHashMap<Integer, MenuComponentObject> components = pagePopulation.getComponents(componentId);

                        int potentialPop = getMenu().calculatePotentialPopulation(componentId, false);
                        int population = components != null ? components.size() : 0;
                        int populationId = (population + 1) + ((getPage() - 1) * potentialPop);

                        System.out.println(componentId +  " | >> page:" + getPage() + ", pop:" + population + ", id: " + populationId + ", pot: " + potentialPop);

                        MenuComponentObject componentObject = component.render(this, slot, populationId, getProps());

                        pagePopulation.registerComponent(componentObject);
                        getComponentMap().put(slot, componentObject);
                        //getComponentPopulation().put(componentId, populationId);
                    }
                }

                getPagePopulations().put(getPage(), pagePopulation);
            }
        }
    }

    public Inventory prepareInventory(){
        Inventory inv;
        if (menu.getTitle() != null){
            String translatedTitle = StringUtils.colored(getPlaceholder().useLinear(menu.getTitle()));
            inv = Bukkit.createInventory(null, getRow()*9, translatedTitle);
        }else{
            if (getInventory() != null){
                inv = getInventory();
            }else {
                inv = Bukkit.createInventory(null, getRow() * 9);
            }
        }

        inv.clear();
        getComponentMap().clear();
        return inv;
    }

    public void setPage(int page){
        this.previousPage = this.page;
        this.page = page;
        boolean isNotSamePage = this.page != this.previousPage;

        if (isNotSamePage) render();
    }

    private void setInventory(Inventory inventory) {
        setPreviousInventory(getInventory());
        this.inventory = inventory;

        if (hasPreviousInventory() && !isSameInventory()){
            List<HumanEntity> viewers = new ArrayList<>(getPreviousInventory().getViewers());
            for (HumanEntity viewer : viewers){
                viewer.closeInventory();
                viewer.openInventory(getInventory());
            }
        }
    }

    public boolean hasPreviousInventory(){
        return getPreviousInventory() != null;
    }

    public boolean isSameInventory() {
        return getInventory() == getPreviousInventory();
    }

    public Menu getMenu() {
        return menu;
    }

    public int getRow() {
        return row;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public int getPage() {
        return page;
    }

    public Inventory getPreviousInventory() {
        return previousInventory;
    }

    public Placeholder getPlaceholder() {
        return placeholder;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public HashMap<String, Integer> getComponentPopulation() {
        return componentPopulation;
    }

    public HashMap<String, Object> getProps() {
        return props;
    }

    public HashMap<String, Object> getStates() {
        return states;
    }

    public HashMap<Integer, MenuComponentObject> getComponentMap() {
        return componentMap;
    }

    public MenuManager getManager() {
        return manager;
    }

    public HashMap<Integer, MenuPagePopulation> getPagePopulations() {
        return pagePopulations;
    }

    private void setPreviousInventory(Inventory previousInventory) {
        this.previousInventory = previousInventory;
    }
}
