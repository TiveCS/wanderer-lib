package io.github.tivecs.wanderer.utils;

import org.bukkit.ChatColor;

import java.util.List;

public class StringUtils {

    public static String colored(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> colored(List<String> l){
        for (int i = 0; i < l.size(); i++){
            l.set(i, colored(l.get(i)));
        }
        return l;
    }

}
