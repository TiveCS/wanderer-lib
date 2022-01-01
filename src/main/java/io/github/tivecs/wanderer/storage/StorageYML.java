package io.github.tivecs.wanderer.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageYML extends Storage{

    private File file = null;
    private final FileConfiguration config;

    /**
     * Create new storage object with file name as storage id.
     * @param file the source file
     */
    public StorageYML(@Nonnull File file) {
        super(file.getName().split("[.]")[0]);
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Create new storage object with {@link FileConfiguration} as source. <br>
     * Using FileConfiguration as source will be unable to save the data into file. <br>
     * Recommended for storage that has behaviour as Read Only.
     * @param id the storage source id
     * @param configuration the storage source
     */
    public StorageYML(String id, FileConfiguration configuration){
        super(id);
        this.config = configuration;
    }

    /**
     * Write data into changes storage.
     * @param path the data path
     * @param value the data value
     * @param overwrite true will overwrite the data path if exists.
     */
    public void writeData(String path, Object value, boolean overwrite){
        if (!overwrite && getData().containsKey(path)){
            return;
        }
        getDataChanges().put(path, value);
    }

    @Override
    public void writeData(HashMap<Object, Object> args) {
        boolean overwrite = true;
        String path = args.get("path").toString();
        Object value = args.get("value");

        if (args.containsKey("overwrite")){
            overwrite = (boolean) args.getOrDefault("overwrite", true);
        }
        if (!overwrite && getData().containsKey(path)){
            return;
        }
        getDataChanges().put(path, value);
    }

    /**
     * Delete data from changes storage.
     * @param path the data path
     */
    public void deleteData(String path){
        getDataChanges().put(path, null);
    }

    @Override
    public void deleteData(HashMap<Object, Object> args) {
        String path = args.get("path").toString();

        getDataChanges().put(path, null);
    }

    /**
     * Load and get data from source, then store the data into temporary storage.<br>
     * If already loaded before, all paths that do not exist on source file will not be replaced.
     * @param overwrite true will clear current temporary storage then replace with newer one.
     */
    public void loadData(boolean overwrite){
       loadData(overwrite, null);
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

    /**
     * Get the data from temporary storage.
     * @param path the data path
     * @return data from temporary storage
     */
    public Object readData(String path){
        return getData().get(path);
    }

    @Override
    public Object readData(HashMap<Object, Object> args) {
        return getData().get(args.get("path"));
    }

    /**
     * Copy all data that registered on changes storage into temporary storage.
     */
    public void saveData(){
        saveData(null);
    }

    @Override
    public void saveData(HashMap<Object, Object> args) {
        for (Map.Entry<Object, Object> entry : getDataChanges().entrySet()){
            getData().put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Save all data from current temporary storage into {@link FileConfiguration}.
     * @param saveToFile true will save all data into source file directly.
     */
    public void saveDataToSource(boolean saveToFile){
        saveDataToSource(null);
        if (saveToFile && getFile() != null){
            try {
                getConfig().save(getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public FileConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }
}
