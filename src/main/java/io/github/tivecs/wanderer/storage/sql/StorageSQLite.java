package io.github.tivecs.wanderer.storage.sql;

import io.github.tivecs.wanderer.sql.SQLiteConnector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class StorageSQLite extends StorageSQL{

    /**
     * Create storage object for new source.
     *
     * @param id        the storage source id.
     * @param dbFile
     */
    public StorageSQLite(JavaPlugin plugin, String id, File dbFile) {
        super(plugin, id, new SQLiteConnector(dbFile));
    }

    protected void saveToSourceInsert(Connection connection) throws SQLException {
        for (Map.Entry<String, SQLRow> rowEntry : getInsertHistory().entrySet()){
            SQLRow row = rowEntry.getValue();
            String rowPath = row.getRowPath();
            SQLTable table = row.getTable();

            StringBuilder query = new StringBuilder("INSERT INTO ").append(table.getTableName()).append(" (")
                    , valueList = new StringBuilder("VALUES(");
            String space = "";
            for (Map.Entry<String, SQLColumn> columnEntry : table.getColumns().entrySet()){
                query.append(space).append(columnEntry.getKey());
                valueList.append(space).append("?");
                space = ", ";
            }
            query.append(") ").append(valueList).append(");");

            getData().put(rowPath, valueToOptional(row));
            getDataChanges().remove(rowPath);

            PreparedStatement statement = connection.prepareStatement(query.toString());
            int columnIndex = 1;
            for (Map.Entry<String, Optional<Object>> dataEntry : row.getRowData().entrySet()){
                String columnPath = rowPath + "." + dataEntry.getKey();
                Optional<Object> value = dataEntry.getValue();

                statement.setObject(columnIndex, value.orElse(null));
                columnIndex++;

                getData().put(columnPath, dataEntry.getValue());
                getDataChanges().remove(columnPath);
            }

            statement.executeUpdate();
        }
        getInsertHistory().clear();
    }

    protected void saveToSourceUpdate(Connection connection) throws SQLException {
        for (Map.Entry<String, SQLRow> rowEntry : getUpdateHistory().entrySet()){
            SQLRow row = rowEntry.getValue();
            String rowPath = row.getRowPath();
            String tableName = row.getTable().getTableName();
            LinkedHashMap<String, Optional<Object>> rowData = row.getRowData(), updatedColumns = row.getUpdatedColumns();

            StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET "),
                    whereQuery = new StringBuilder(" WHERE ");
            String space = "", whereSpace = "";
            for (String column : rowData.keySet()){
                if (updatedColumns.containsKey(column)){
                    query.append(space).append(column).append("=?");
                    space = ", ";
                }
                whereQuery.append(whereSpace).append(column).append("=?");
                whereSpace = " AND ";
            }
            query.append(whereQuery);

            PreparedStatement statement = connection.prepareStatement(query.toString());

            int i = updatedColumns.size() + 1;
            int j = 1;
            for (Map.Entry<String, Optional<Object>> dataEntry : rowData.entrySet()){
                statement.setObject(i, dataEntry.getValue().orElse(null));
                i++;

                String updatedColumnPath = rowPath + "." + dataEntry.getKey();
                if (getDataChanges().containsKey(updatedColumnPath)) {
                    Optional<Object> updatedValue = getDataChanges().get(updatedColumnPath);
                    statement.setObject(j, updatedValue.orElse(null));
                    j++;
                    getDataChanges().remove(updatedColumnPath);
                    getData().put(updatedColumnPath, updatedValue);
                    row.set(dataEntry.getKey(), updatedValue.orElseGet(Optional::empty));
                }
            }

            statement.executeUpdate();
            updatedColumns.clear();
        }
        getUpdateHistory().clear();
    }

    protected void saveToSourceDelete(Connection connection) throws SQLException {
        for (Map.Entry<String, SQLRow> rowEntry : getDeleteHistory().entrySet()){
            SQLRow row = rowEntry.getValue();
            SQLTable table = row.getTable();

            StringBuilder query = new StringBuilder("DELETE FROM ").append(table.getTableName()).append(" WHERE ");
            String space = "";
            for (String columnName : table.getColumns().keySet()){
                query.append(space).append(columnName).append("=?");
                space = " AND ";
            }

            PreparedStatement statement = connection.prepareStatement(query.toString());
            int i = 1;
            for (Map.Entry<String, Optional<Object>> rowData : row.getRowData().entrySet()){
                statement.setObject(i, rowData.getValue().orElse(null));
                i++;

                getData().remove(row.getRowPath() + "." + rowData.getKey());
            }
            statement.executeUpdate();
            getData().remove(row.getRowPath());
        }
        getDeleteHistory().clear();
        System.out.println(getData());
    }

    @Override
    public void saveDataToSource(@Nullable HashMap<Object, Object> args) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            try {
                Connection connection = getConnector().getConnection();

                saveToSourceInsert(connection);
                saveToSourceUpdate(connection);
                saveToSourceDelete(connection);

                getDataChanges().clear();

                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        });

    }
}
