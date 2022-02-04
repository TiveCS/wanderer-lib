package io.github.tivecs.wanderer.storage.sql;

import io.github.tivecs.wanderer.sql.SQLConnector;
import io.github.tivecs.wanderer.storage.Storage;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.*;

public abstract class StorageSQL extends Storage {

    public enum ModificationType{
        INSERT, UPDATE, DELETE
    }

    private SQLConnector connector;
    private JavaPlugin plugin;
    private HashMap<String, SQLTable> tables = new HashMap<>();

    private LinkedHashMap<String, SQLRow> insertHistory = new LinkedHashMap<>(),
            updateHistory = new LinkedHashMap<>(),
            deleteHistory = new LinkedHashMap<>();

    /**
     * Create storage object for new source.
     *
     * @param id the storage source id.
     */
    public StorageSQL(JavaPlugin plugin, String id, SQLConnector connector) {
        super(id);
        this.plugin = plugin;
        this.connector = connector;
    }

    @Override
    public void loadData(boolean overwrite, @Nullable HashMap<Object, Object> args) {
        loadData(overwrite);
    }

    public void loadData(boolean overwrite){
        if (overwrite){
            getData().clear();
            getTables().clear();
            getDataChanges().clear();
        }
        try {
            Connection connection = getConnector().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            Statement statement = connection.createStatement();

            ResultSet tables = metaData.getTables(null, null, "%", null);
            while(tables.next()){
                String tableName = tables.getString("TABLE_NAME");
                StringBuilder query = new StringBuilder("SELECT * FROM ").append(tableName);

                SQLTable table = new SQLTable(this, tableName);
                table.initColumn(metaData);
                table.loadTable(connection.createStatement().executeQuery(query.toString()));

                getTables().put(tableName, table);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public LinkedHashSet<Integer> findRows(String tableName, String columnName, Object columnValue){
        SQLTable table = getTables().get(tableName);
        if (table != null){
            return table.findRowIds(columnName, columnValue);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void writeData(HashMap<Object, Object> args) {
        ModificationType modificationType = ModificationType.valueOf(args.get("type").toString().toUpperCase(Locale.ROOT));
        Object value = args.get("value");
        Object condition = args.get("conditions");
        String tableName = (String) args.get("table");
        String columnName = (String) args.get("column");
        Optional<Object> valueOptional = valueToOptional(value);
        Optional<Object> conditionOptional = valueToOptional(condition);

        switch (modificationType){
            case DELETE:
                delete(tableName, (LinkedHashMap<String, Object>) condition);
                break;
            case UPDATE:
                if (valueOptional.isEmpty()) return;
                LinkedHashMap<String, Object> columnCondition = null;
                if (conditionOptional.isPresent() && (conditionOptional.get() instanceof LinkedHashMap<?, ?>)) {
                     columnCondition = ((LinkedHashMap<String, Object>) conditionOptional.get());
                }

                if (columnCondition != null) {
                    if (valueOptional.get() instanceof LinkedHashMap<?, ?>) {
                        LinkedHashMap<String, Object> columnValues = ((LinkedHashMap<String, Object>) valueOptional.get());

                        update(tableName, columnValues, columnCondition);
                    } else {
                        update(tableName, columnName, valueOptional.get(), columnCondition);
                    }
                }
                break;
            case INSERT:
                if (valueOptional.isEmpty()) return;
                insert(tableName, (LinkedHashMap<String, Object>) valueOptional.get());
                break;
        }
    }

    private void insert(String tableName, LinkedHashMap<String, Object> columnValues){
        SQLTable table = getTables().get(tableName);
        if (table != null){
            LinkedHashMap<String, SQLColumn> columns = table.getColumns();
            if ((columns.size() == columnValues.size()) && columns.keySet().containsAll(columnValues.keySet())){
                SQLRow row = table.insert(columnValues);
                getInsertHistory().put(row.getRowPath(), row);
            }else{
                System.out.println("Row column data is not same with the table!");
            }
        }else{
            System.out.println("Table is not found!");
        }
    }

    private void update(String tableName, String columnName, Object columnValue, LinkedHashMap<String, Object> columnValuesConditions){
        SQLTable table = getTables().get(tableName);
        if (table != null) {
            LinkedHashMap<String, SQLColumn> columns = table.getColumns();
            if (columns.keySet().containsAll(columnValuesConditions.keySet())){
                LinkedHashMap<String, Object> columnValues = new LinkedHashMap<>();
                columnValues.put(columnName, columnValue);
                LinkedHashSet<SQLRow> affectedRows = table.update(columnValues, columnValuesConditions);

                for (SQLRow row : affectedRows){
                    getUpdateHistory().put(row.getRowPath(), row);
                }
            }
        }
    }

    private void update(String tableName, LinkedHashMap<String, Object> columnValues, LinkedHashMap<String, Object> columnValuesConditions){
        SQLTable table = getTables().get(tableName);
        if (table != null){
            LinkedHashMap<String, SQLColumn> columns = table.getColumns();
            if (columns.keySet().containsAll(columnValues.keySet())){
                table.update(columnValues, columnValuesConditions);
            }
        }
    }

    @Override
    public Optional<Object> readData(HashMap<Object, Object> args) {
        String path = args.get("path").toString();

        return getData().get(path);
    }

    private LinkedHashMap<Integer, SQLRow> select(){
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteData(HashMap<Object, Object> args) {
        String tableName = (String) args.get("table");
        Object conditions = args.get("conditions");

        delete(tableName, (LinkedHashMap<String, Object>) conditions);
    }

    private void delete(String tableName, LinkedHashMap<String, Object> columnValuesConditions){
        SQLTable table = getTables().get(tableName);
        if (table != null){
            LinkedHashMap<String, SQLColumn> columns = table.getColumns();
            if (columns.keySet().containsAll(columnValuesConditions.keySet())){
                table.delete(columnValuesConditions);
            }
        }
    }

    @Override
    public void saveData(@Nullable HashMap<Object, Object> args) {
        for (Map.Entry<String, SQLRow> updateEntry : getUpdateHistory().entrySet()){
            SQLRow row = updateEntry.getValue();
            for (Map.Entry<String, Optional<Object>> updatedCol : row.getUpdatedColumns().entrySet()){
                row.set(updatedCol.getKey(), updatedCol.getValue());
            }
            row.getUpdatedColumns().clear();
        }
        for (Map.Entry<String, SQLRow> deleteEntry : getDeleteHistory().entrySet()){
            String rowPath = deleteEntry.getKey();
            getData().remove(rowPath);
            for (String column : deleteEntry.getValue().getRowData().keySet()){
                getData().remove(rowPath + "." + column);
            }
            getDataChanges().remove(rowPath);
        }
        getData().putAll(getDataChanges());
        getDataChanges().clear();
    }

    @Override
    public Object convertData(HashMap<Object, Object> args) {
        return null;
    }

    public SQLConnector getConnector() {
        return connector;
    }

    public HashMap<String, SQLTable> getTables() {
        return tables;
    }

    @Override
    public Map<Object, Optional<Object>> initializeDataMap() {
        return new LinkedHashMap<>();
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public LinkedHashMap<String, SQLRow> getInsertHistory() {
        return insertHistory;
    }

    public LinkedHashMap<String, SQLRow> getDeleteHistory() {
        return deleteHistory;
    }

    public LinkedHashMap<String, SQLRow> getUpdateHistory() {
        return updateHistory;
    }
}
