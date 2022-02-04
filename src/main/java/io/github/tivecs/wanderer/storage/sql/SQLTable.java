package io.github.tivecs.wanderer.storage.sql;

import io.github.tivecs.wanderer.storage.Storage;

import javax.annotation.Nonnull;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SQLTable {

    private StorageSQL database;
    private String tableName;

    private int lastRowId = 0;
    private LinkedHashMap<String, SQLColumn> columns = new LinkedHashMap<>();
    private LinkedHashMap<Integer, SQLRow> rows = new LinkedHashMap<>();

    public SQLTable(@Nonnull StorageSQL database, @Nonnull String tableName){
        this.database = database;
        this.tableName = tableName;
    }

    public void initColumn(@Nonnull DatabaseMetaData metaData) throws SQLException {
        ResultSet rsColumn = metaData.getColumns(null, null, tableName, null);

        getColumns().clear();
        while(rsColumn.next()){
            String columnName = rsColumn.getString("COLUMN_NAME");
            SQLColumn column = new SQLColumn(this, columnName);
            getColumns().put(columnName, column);
        }
    }

    public SQLTable loadTable(@Nonnull ResultSet selectResultSet) throws SQLException {
        Set<Map.Entry<String, SQLColumn>> columns = getColumns().entrySet();

        while(selectResultSet.next()){
            int rowId = selectResultSet.getRow();
            SQLRow row = new SQLRow(this, rowId);

            for (Map.Entry<String, SQLColumn> entry : columns){
                Object columnValue = selectResultSet.getObject(entry.getKey());
                SQLColumn column = entry.getValue();

                registerRowToColumn(row, column, columnValue);
            }

            registerRowToTable(row);
            registerRowToDatabase(row);
        }
        return this;
    }

    public SQLRow insert(LinkedHashMap<String, Object> columnValues){
        int rowId = getLastRowId() + 1;
        SQLRow row = new SQLRow(this, rowId);

        for (Map.Entry<String, Object> entry : columnValues.entrySet()){
            registerRowToColumn(row, getColumns().get(entry.getKey()), entry.getValue());
        }

        registerRowToTable(row);
        updateRowToDatabase(row, false);
        getDatabase().getDataChanges().put(row.getRowPath(), Storage.valueToOptional(row));
        return row;
    }

    public LinkedHashSet<SQLRow> update(LinkedHashMap<String, Object> columnValues, LinkedHashMap<String, Object> columnValuesConditions){
        LinkedHashSet<SQLRow> affectedRows = new LinkedHashSet<>();
        LinkedHashSet<Integer> affectedRowsIds = searchRowIds(columnValuesConditions);

        for (int rowId : affectedRowsIds){
            SQLRow row = getRows().get(rowId);
            for (Map.Entry<String, Object> colValues : columnValues.entrySet()){
                SQLColumn column = getColumns().get(colValues.getKey());
                row.update(colValues.getKey(), colValues.getValue());
                updateRowToColumn(row, column, colValues.getValue());
            }
            affectedRows.add(row);
        }

        return affectedRows;
    }

    public LinkedHashSet<SQLRow> delete(LinkedHashMap<String, Object> columnValuesConditions){
        LinkedHashSet<SQLRow> affectedRows = new LinkedHashSet<>();
        LinkedHashSet<Integer> affectedRowsIds = searchRowIds(columnValuesConditions);

        for (int rowId : affectedRowsIds){
            SQLRow row = getRows().get(rowId);

            // TODO Delete function here
            // TODO Data Change to Optional.empty
            // TODO Row removed from Column and Table
            // TODO Add deleted Row to DeleteHistory without data loss

            getDatabase().getDataChanges().put(row.getRowPath(), Optional.empty());

            getRows().remove(rowId);
            for (SQLColumn column : getColumns().values()){
                column.removeRowFromValueList(row);
            }

            getDatabase().getDeleteHistory().put(row.getRowPath(), row);

            affectedRows.add(row);
        }

        return affectedRows;
    }

    private LinkedHashSet<Integer> searchRowIds(LinkedHashMap<String, Object> columnValuesConditions){
        LinkedHashSet<Integer> rowIds = new LinkedHashSet<>();
        LinkedHashSet<LinkedHashSet<Integer>> valuesRows = new LinkedHashSet<>();

        for (Map.Entry<String, Object> colEntry : columnValuesConditions.entrySet()){
            SQLColumn column = getColumns().get(colEntry.getKey());
            Optional<Object> value = Storage.valueToOptional(colEntry.getValue());
            LinkedHashSet<Integer> rows = column.getValueRowSet().get(value);

            if (rows == null) continue;

            rowIds.addAll(rows);
            valuesRows.add(rows);
        }

        for (LinkedHashSet<Integer> rows : valuesRows){
            rowIds.retainAll(rows);
        }

        return rowIds;
    }

    private void updateRowToDatabase(SQLRow row, boolean compare){
        row.updateToDatabase(compare);
    }

    private void registerRowToDatabase(SQLRow row){
        row.registerToDatabase();
    }

    private void registerRowToTable(SQLRow row){
        int rowId = row.getRowId();
        setLastRowId(rowId);
        getRows().put(rowId, row);
    }

    private void updateRowToColumn(SQLRow row, SQLColumn column, Object newColumnValue){
        column.removeRowFromValueList(row);
        column.addRowToValueList(row.getRowId(), newColumnValue);
    }

    private void registerRowToColumn(SQLRow row, SQLColumn column, Object columnValue){
        column.addRowToValueList(row.getRowId(), columnValue);
        row.set(column.getColumnName(), columnValue);
    }

    public LinkedHashSet<Integer> findRowIds(String columnName, Object columnValue){
        SQLColumn column = getColumns().get(columnName);
        if (column != null){
            return column.getValueRowSet().get(Storage.valueToOptional(columnValue));
        }
        return null;
    }

    public LinkedHashMap<Integer, SQLRow> findRows(String columnName, Object columnValue){
        LinkedHashSet<Integer> rowIds = findRowIds(columnName, columnValue);
        if (rowIds != null){
            LinkedHashMap<Integer, SQLRow> rows = new LinkedHashMap<>();
            for (int rowId : rowIds){
                SQLRow row = getRows().get(rowId);
                rows.put(rowId, row);
            }
            return rows;
        }
        return null;
    }

    protected void setRow(SQLRow row){
        StorageSQL db = getDatabase();
        StringBuilder path = new StringBuilder(getTableName()).append(".").append(row.getRowId());

        Optional<Object> rowOptional = Optional.of(row);
        getDatabase().getData().put(path.toString(), rowOptional);
    }

    private void setLastRowId(int lastRowId) {
        this.lastRowId = lastRowId;
    }

    public LinkedHashMap<Integer, SQLRow> getRows() {
        return rows;
    }

    public LinkedHashMap<String, SQLColumn> getColumns() {
        return columns;
    }

    public String getTableName() {
        return tableName;
    }

    public int getLastRowId() {
        return lastRowId;
    }

    public StorageSQL getDatabase() {
        return database;
    }

}
