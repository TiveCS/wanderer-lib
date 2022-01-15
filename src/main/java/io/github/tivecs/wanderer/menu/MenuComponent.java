package io.github.tivecs.wanderer.menu;

import io.github.tivecs.wanderer.menu.events.ComponentPostRenderEvent;
import io.github.tivecs.wanderer.menu.events.ComponentPreRenderEvent;
import io.github.tivecs.wanderer.utils.StringUtils;
import io.github.tivecs.wanderer.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class MenuComponent {

    private final String componentId;
    private final char mapId;
    private final HashMap<String, Object> states = new HashMap<>();

    private XMaterial material = XMaterial.STONE;
    private String displayName = null;
    private int amount = 1;
    private List<String> lore = null;

    private MenuComponentInteraction interaction = null;

    public MenuComponent(String componentId, char mapId){
        this.componentId = componentId;
        this.mapId = mapId;
    }

    public MenuComponent setState(String key, Object value){
        getStates().put(key, value);
        return this;
    }

    public ItemStack createItem(){
        ItemStack item = getMaterial().parseItem();
        ItemMeta meta = item.getItemMeta();

        if (getDisplayName() != null) meta.setDisplayName(StringUtils.colored(getDisplayName()));
        if (getLore() != null) meta.setLore(StringUtils.colored(getLore()));
        item.setItemMeta(meta);
        item.setAmount(getAmount());

        return item;
    }

    public MenuComponentObject render(@Nonnull MenuObject menuObject, int slot, int populationId, @Nullable HashMap<String, Object> props){
        MenuComponentObject object = new MenuComponentObject(menuObject, this, slot, populationId);

        ComponentPreRenderEvent preRenderEvent = new ComponentPreRenderEvent(menuObject, this, object, props);
        Bukkit.getPluginManager().callEvent(preRenderEvent);

        if (props != null) object.getProps().putAll(props);
        object.getStates().putAll(getStates());
        object.updatePlaceholder();
        object.setBaseItem(createItem());

        object.render();
        ComponentPostRenderEvent postRenderEvent = new ComponentPostRenderEvent(menuObject, this, object);
        Bukkit.getPluginManager().callEvent(postRenderEvent);
        return object;
    }

    public MenuComponent setMaterial(XMaterial material) {
        this.material = material;
        return this;
    }

    public MenuComponent setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public MenuComponent setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public MenuComponent setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public MenuComponent setInteraction(MenuComponentInteraction interaction) {
        this.interaction = interaction;
        return this;
    }

    public List<String> getLore() {
        return lore;
    }

    public MenuComponentInteraction getInteraction() {
        return interaction;
    }

    public int getAmount() {
        return amount;
    }

    public XMaterial getMaterial() {
        return material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public HashMap<String, Object> getStates() {
        return states;
    }

    public String getComponentId() {
        return componentId;
    }

    public char getMapId() {
        return mapId;
    }
}
