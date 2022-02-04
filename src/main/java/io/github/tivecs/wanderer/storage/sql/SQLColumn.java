package io.github.tivecs.wanderer.storage.sql;

import io.github.tivecs.wanderer.storage.Storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Optional;

public class SQLColumn {

    private SQLTable table;
    private String columnName;

    private LinkedHashMap<Optional<Object>, LinkedHashSet<Integer>> valueRowSet = new LinkedHashMap<>();

    public SQLColumn(@Nonnull SQLTable table, @Nonnull String columnName){
        this.table = table;
        this.columnName = columnName;
    }

    public void addRowToValueList(int rowId, @Nullable Object value){
        Optional<Object> valueOptional = Storage.valueToOptional(value);
        LinkedHashSet<Integer> rowIdMap = getValueRowSet().computeIfAbsent(valueOptional, k -> new LinkedHashSet<>());

        rowIdMap.add(rowId);
    }

    public void removeRowFromValueList(@Nonnull SQLRow row){
        Optional<Object> valueOptional = row.get(getColumnName());
        LinkedHashSet<Integer> rowIdMap = getValueRowSet().get(valueOptional);

        rowIdMap.remove(row.getRowId());
        if (rowIdMap.isEmpty()){
            getValueRowSet().remove(valueOptional);
        }
    }

    public SQLTable getTable() {
        return table;
    }

    public String getColumnName() {
        return columnName;
    }

    public LinkedHashMap<Optional<Object>, LinkedHashSet<Integer>> getValueRowSet() {
        return valueRowSet;
    }

    @Override
    public String toString() {
        return "SQLColumn{" +
                "columnName='" + columnName + '\'' +
                ", valueRowSet=" + valueRowSet +
                '}';
    }
}
