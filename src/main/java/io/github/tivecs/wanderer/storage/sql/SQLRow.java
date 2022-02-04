package io.github.tivecs.wanderer.storage.sql;

import io.github.tivecs.wanderer.storage.Storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SQLRow {

    private SQLTable table;
    private int rowId;
    private String rowPath;

    private LinkedHashMap<String, Optional<Object>> updatedColumns = new LinkedHashMap<>();
    private LinkedHashMap<String, Optional<Object>> rowData = new LinkedHashMap<>();

    public SQLRow(@Nonnull SQLTable table, int rowId){
        this.table = table;
        this.rowId = rowId;
        this.rowPath = new StringBuilder(table.getTableName()).append(".").append(rowId).toString();
    }

    protected void registerToDatabase(){
        StorageSQL database = getTable().getDatabase();
        Map<Object, Optional<Object>> databaseStorage = database.getData();

        databaseStorage.put(getRowPath(), Storage.valueToOptional(this));
        for (Map.Entry<String, Optional<Object>> data : getRowData().entrySet()){
            databaseStorage.put(getRowPath() + "." + data.getKey(), data.getValue());
        }
    }

    public void updateToDatabase(boolean compare){
        StorageSQL database = getTable().getDatabase();
        Map<Object, Optional<Object>> databaseChanges = database.getDataChanges(), databaseData = database.getData();

        for (Map.Entry<String, Optional<Object>> data : getRowData().entrySet()){
            String columnPath = getRowPath() + "." + data.getKey();
            Optional<Object> columnData = data.getValue();

            if (compare){
                Optional<Object> dbChangesValue = databaseChanges.get(columnPath), dbDataValue = databaseData.get(columnData);

                if ((dbChangesValue.isPresent() && dbChangesValue.equals(columnData)) && (dbDataValue.isPresent() && dbDataValue.equals(columnData))){
                    continue;
                }
            }
            databaseChanges.put(columnPath, columnData);
        }
    }

    public void updateColumnToDatabase(String column, Object value){
        StorageSQL database = getTable().getDatabase();
        Map<Object, Optional<Object>> databaseChanges = database.getDataChanges();
        String columnPath = getRowPath() + "." + column;

        Optional<Object> columnData = Storage.valueToOptional(value);
        Optional<Object> changesValue = databaseChanges.getOrDefault(columnPath, Optional.empty());
        if (!changesValue.equals(columnData)){
            databaseChanges.put(columnPath, columnData);
        }
    }

    public void update(@Nonnull String column, Object value){
        getUpdatedColumns().put(column, Storage.valueToOptional(value));
        updateColumnToDatabase(column, value);
    }

    public Optional<Object> get(@Nonnull String column){
        return getRowData().get(column);
    }

    protected void set(@Nonnull String column, @Nullable Object value){
        getRowData().put(column, Storage.valueToOptional(value));
    }

    public LinkedHashMap<String, Optional<Object>> getRowData() {
        return rowData;
    }

    public int getRowId() {
        return rowId;
    }

    public SQLTable getTable() {
        return table;
    }

    public String getRowPath() {
        return rowPath;
    }

    public LinkedHashMap<String, Optional<Object>> getUpdatedColumns() {
        return updatedColumns;
    }

    @Override
    public String toString() {
        return "SQLRow{" +
                "table=" + table.getTableName() +
                ", rowId=" + rowId +
                ", rowData=" + rowData +
                '}';
    }
}
