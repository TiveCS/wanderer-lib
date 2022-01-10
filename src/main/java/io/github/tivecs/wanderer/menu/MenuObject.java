package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.language.Placeholder;
import io.github.tivecs.wanderer.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MenuObject {

    private final Menu menu;
    private final Placeholder placeholder = new Placeholder();
    private final HashMap<String, Object> props;
    private HashMap<String, Object> states = new HashMap<>();

    private Inventory inventory = null;
    private int row = 3;
    private int page = -1;
    private HashMap<Integer, MenuComponentObject> componentMap = new HashMap<>();

    public MenuObject(@Nonnull Menu menu, @Nullable HashMap<String, Object> props){
        this.menu = menu;
        this.props = props != null ? new HashMap<>(props) : null;

        int rowSize = menu.getMapping().size();
        if (rowSize > 0){
            this.row = rowSize;
        }

    }

    // TODO Render current page
    public void render(){
        setInventory(prepareInventory());
        mappingComponent();
        visualizeMap();
    }

    public void visualizeMap(){
        getInventory().clear();
        for (Map.Entry<Integer, MenuComponentObject> entry : getComponentMap().entrySet()){
            getInventory().setItem(entry.getKey(), entry.getValue().getCurrentItem());
        }
    }

    // TODO Add component render event
    public void mappingComponent(){
        getComponentMap().clear();
        if (getInventory() != null){
            for (int row = 0; row < getRow(); row++){
                String map = getMenu().getMapping().get(row);
                char[] mapIds = map.toCharArray();

                for (int column = 0; column < 9; column++){
                    int slot = (row*9) + column;
                    char mapId = mapIds[column];
                    MenuComponent component = getMenu().findComponent(mapId);

                    if (component != null){
                        MenuComponentObject componentObject = component.render(this, getProps());
                        componentObject.render();
                        getComponentMap().put(slot, componentObject);
                    }
                }
            }
        }
    }

    public Inventory prepareInventory(){
        Inventory inv;
        if (menu.getTitle() != null){
            inv = Bukkit.createInventory(null, getRow()*9, StringUtils.colored(menu.getTitle()));
        }else{
            inv = Bukkit.createInventory(null, getRow()*9);
        }
        return inv;
    }

    public void setPage(int page){
        boolean isNotSamePage = this.page != page;
        this.page = page;

        if (isNotSamePage) render();
    }

    private void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getRow() {
        return row;
    }

    public int getPage() {
        return page;
    }

    public Placeholder getPlaceholder() {
        return placeholder;
    }

    public Inventory getInventory() {
        return inventory;
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
}
