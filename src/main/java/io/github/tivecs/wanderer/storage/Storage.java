package io.github.tivecs.wanderer.storage;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class Storage {

    private String id;

    private Map<Object, Object> data;

    public Storage(String id){
        this.id = id;
        this.data = initializeDataMap();
    }

    public abstract void writeData(HashMap<Object, Object> args);
    public abstract void deleteData(HashMap<Object, Object> args);
    public abstract Object readData(HashMap<Object, Object> args);
    public abstract void loadData(boolean overwrite, @Nullable HashMap<Object, Object> args);
    public abstract void saveData(@Nullable HashMap<Object, Object> args);
    public abstract void saveDataToSource(@Nullable HashMap<Object, Object> args);

    public abstract Object convertData(HashMap<Object, Object> args); // TODO make conversion between other storage type

    public abstract Map<Object, Object> initializeDataMap();

    public Map<Object, Object> getData() {
        return data;
    }

    public String getId() {
        return id;
    }
}
