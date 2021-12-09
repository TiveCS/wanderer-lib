package io.github.tivecs.wanderer.storage;

import javax.annotation.Nullable;
import java.util.HashMap;

public class StorageManager {

    private HashMap<String, Storage> storages = new HashMap<>();

    public StorageManager(){}

    public void registerStorage(Storage storage){
        registerStorage(storage, false, null);
    }
    public void registerStorage(Storage storage, boolean load){
        registerStorage(storage, load, null);
    }
    public void registerStorage(Storage storage, boolean load, @Nullable HashMap<Object, Object> args){
        getStorages().put(storage.getId(), storage);
        if (load){
            storage.loadData(true, args);
        }
    }

    public void loadData(Storage storage, boolean overwrite, @Nullable HashMap<Object, Object> args){
        storage.loadData(overwrite, args);
    }

    public void writeData(Storage storage, HashMap<Object, Object> args){
        storage.writeData(args);
    }

    public void deleteData(Storage storage, HashMap<Object, Object> args){
        storage.deleteData(args);
    }

    public Object readData(Storage storage, HashMap<Object, Object> args) {
        return storage.readData(args);
    }

    public void saveData(Storage storage, HashMap<Object, Object> args){
        storage.saveData(args);
    }

    public void saveDataToSource(Storage storage, HashMap<Object, Object> args){
        storage.saveDataToSource(args);
    }

    public Object convertData(Storage storage){
        return null;
    }

    public Storage getStorage(String id){
        return getStorages().get(id);
    }

    public HashMap<String, Storage> getStorages() {
        return storages;
    }
}
