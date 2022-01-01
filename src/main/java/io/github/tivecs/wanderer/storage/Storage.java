package io.github.tivecs.wanderer.storage;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for any storage type implementation.
 */
public abstract class Storage {

    private final String id;

    /**
     * Act as temporary storage.
     */
    private final Map<Object, Object> data;

    /**
     * Act as changes storage for any changes on specific data path.
     */
    private final Map<Object, Object> dataChanges;

    /**
     * Create storage object for new source.
     * @param id the storage source id.
     */
    public Storage(String id){
        this.id = id;
        this.data = initializeDataMap();
        this.dataChanges = initializeDataMap();
    }

    /**
     * Write data into changes storage.
     * @param args settings for write data.
     */
    public abstract void writeData(HashMap<Object, Object> args);

    /**
     * Delete data from changes storage.
     * @param args settings for delete data.
     */
    public abstract void deleteData(HashMap<Object, Object> args);

    /**
     * Get the data from temporary storage.
     * @param args settings for reading data.
     * @return data from temporary storage.
     */
    public abstract Object readData(HashMap<Object, Object> args);

    /**
     * Load and get data from source, then store the data into temporary storage.
     * @param overwrite will overwrite current temporary storage if true.
     * @param args settings for load the data.
     */
    public abstract void loadData(boolean overwrite, @Nullable HashMap<Object, Object> args);

    /**
     * Copy all data that registered on changes storage into temporary storage.
     * @param args settings for save data.
     */
    public abstract void saveData(@Nullable HashMap<Object, Object> args);

    /**
     * Save all data from current temporary storage into source.
     * @param args settings for save data to source.
     */
    public abstract void saveDataToSource(@Nullable HashMap<Object, Object> args);

    /**
     * Convert all data from temporary storage into wanted storage type.
     * @param args settings for converting data.
     * @return the converted temporary storage.
     */
    public abstract Object convertData(HashMap<Object, Object> args); // TODO make conversion between other storage type

    /**
     * Set the Map type of temporary storage and changes storage.
     * @return map type of storage
     */
    public abstract Map<Object, Object> initializeDataMap();

    /**
     * Get the temporary storage.
     * @return temporary storage
     */
    public Map<Object, Object> getData() {
        return data;
    }

    /**
     * Get the changes storage.
     * @return changes storage
     */
    public Map<Object, Object> getDataChanges() {
        return dataChanges;
    }

    public String getId() {
        return id;
    }
}
