package io.github.tivecs.wanderer.language;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LanguageManager {

    private final JavaPlugin plugin;
    private final HashSet<String> messagePaths = new HashSet<>();
    private final HashMap<String, Language> languages = new HashMap<>();

    private Language defaultLanguage = null;

    public LanguageManager(@Nonnull JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void sendMessage(Player player, String messagePath){sendMessage(player, messagePath, getDefaultLanguage(), false);}
    public void sendMessage(Player player, String messagePath, Language language){sendMessage(player, messagePath, language, false);}
    public void sendMessage(Player player, String messagePath, Language language, boolean colored){
        Message msgObject = language.getMessage(messagePath);
        List<String> msg = msgObject.getMessages();
        for (String s : msg){
            String text = s;
            if (colored){
                text = ChatColor.translateAlternateColorCodes('&', text);
            }
            player.sendMessage(text);
        }
    }

    public boolean checkMessageCompletion(@Nonnull Language language){
        HashMap<String, Message> msg = language.getMessages();
        for (String messagePath : getMessagePaths()){
            if (!msg.containsKey(messagePath)) return false;
        }
        return true;
    }

    public void addMessage(@Nonnull Language language, @Nonnull String messagePath, @Nonnull String... messages){
        getMessagePaths().add(messagePath);
        language.addMessage(messagePath);
    }

    public void setDefaultLanguage(String langId){
        setDefaultLanguage(getLanguages().get(langId));
    }

    public void setDefaultLanguage(Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Language getLanguage(@Nonnull String langId){
        return getLanguages().get(langId);
    }

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public HashMap<String, Language> getLanguages() {
        return languages;
    }

    public HashSet<String> getMessagePaths() {
        return messagePaths;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
