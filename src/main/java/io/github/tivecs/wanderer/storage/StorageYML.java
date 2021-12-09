package io.github.tivecs.wanderer.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageYML extends Storage{

    private File file = null;
    private FileConfiguration config = null;
    private Map<Object, Object> dataChanges;

    public StorageYML(@Nonnull File file) {
        super(file.getName().split("[.]")[0]);
        this.dataChanges = initializeDataMap();
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void writeData(HashMap<Object, Object> args) {
        String path = args.get("path").toString();
        Object value = args.get("value");

        getDataChanges().put(path, value);
    }

    @Override
    public void deleteData(HashMap<Object, Object> args) {
        String path = args.get("path").toString();

        getDataChanges().put(path, null);
    }

    @Override
    public void loadData(boolean overwrite, @Nullable HashMap<Object, Object> args) {
        if (overwrite) {
            getData().clear();
        }

        for (String path : getConfig().getKeys(true)){
            getData().put(path, getConfig().get(path));
        }
    }

    @Override
    public Object readData(HashMap<Object, Object> args) {
        return getData().get(args.get("path"));
    }

    @Override
    public void saveData(HashMap<Object, Object> args) {
        for (Map.Entry<Object, Object> entry : getDataChanges().entrySet()){
            getData().put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void saveDataToSource(HashMap<Object, Object> args) {
        for (Object path : getDataChanges().keySet()){
            getConfig().set(path.toString(), getDataChanges().get(path));
        }
    }


    @Override
    public Object convertData(HashMap<Object, Object> args) {
        return null;
    }

    @Override
    public Map<Object, Object> initializeDataMap() {
        return new ConcurrentHashMap<>();
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Map<Object, Object> getDataChanges() {
        return dataChanges;
    }
}
