package io.github.tivecs.wanderer.language;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Placeholder implements Cloneable {

    private final LinkedHashMap<String, String> placeholders = new LinkedHashMap<>();

    public Placeholder(){}

    public void set(String placeholder, String value){
        getPlaceholders().put(placeholder, value);
    }

    public void delete(String placeholder){
        getPlaceholders().remove(placeholder);
    }

    public ItemStack useForItem(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (meta.hasDisplayName()) meta.setDisplayName(useLinear(meta.getDisplayName()));
            if (meta.hasLore()) {
                meta.setLore(useForList(meta.getLore()));
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public List<String> useForList(List<String> list){
        for (int i = 0; i < list.size(); i++){
            list.set(i, useLinear(list.get(i)));
        }
        return list;
    }

    public String useLinear(String text){
        for (Map.Entry<String, String> entry: getPlaceholders().entrySet()){
            StringBuilder path = new StringBuilder().append("%").append(entry.getKey()).append("%");
            text = text.replace(path, entry.getValue());
        }
        return text;
    }

    public LinkedHashMap<String, String> getPlaceholders() {
        return placeholders;
    }

    @Override
    protected Placeholder clone() throws CloneNotSupportedException {
        return (Placeholder) super.clone();
    }
}
