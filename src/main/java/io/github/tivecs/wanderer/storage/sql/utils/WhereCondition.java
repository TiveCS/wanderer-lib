package io.github.tivecs.wanderer.storage.sql.utils;

import javax.annotation.Nonnull;
import java.util.LinkedHashSet;

public class WhereCondition {

    public enum ConditionOperator{
        EQUAL("="), NOT_EQUAL("<>"),
        LESS("<"), LESS_OR_EQUAL("<="),
        GREATER(">"), GREATER_OR_EQUAL(">="),
        BETWEEN("BETWEEN"), IN("IN"), LIKE("LIKE");

        private final String symbol;
        ConditionOperator(String symbol){
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public enum CombinationOperator{
        OR, AND
    }

    protected enum WhereConditionType{
        SINGLE, COMBINATION
    }

    private WhereConditionType whereConditionType;
    private boolean usingNotOperator = false;

    private String column = null;
    private ConditionOperator conditionOperator = null;
    private Object value = null;

    private WhereCondition condition1, condition2;
    private CombinationOperator combinationOperator;

    public WhereCondition(@Nonnull String column, @Nonnull ConditionOperator condition, @Nonnull Object value){
        this.column = column;
        this.conditionOperator = condition;
        this.value = value;
        this.whereConditionType = WhereConditionType.SINGLE;
    }

    public WhereCondition(@Nonnull WhereCondition condition1, @Nonnull CombinationOperator combination, @Nonnull WhereCondition condition2){
        this.condition1 = condition1;
        this.combinationOperator = combination;
        this.condition2 = condition2;
        this.whereConditionType = WhereConditionType.COMBINATION;
    }

    public String singleToString(boolean usePreparedMark){
        if (!getWhereConditionType().equals(WhereConditionType.SINGLE)) return null;
        StringBuilder builder = new StringBuilder();

        if (isUsingNotOperator()){
            builder.append("NOT ");
        }

        builder.append(this.column).append(" ").append(this.conditionOperator.getSymbol()).append(" ");

        if (usePreparedMark){
            builder.append("?");
        }else {
            if (value instanceof Number) {
                builder.append(value);
            } else {
                builder.append("'").append(value.toString()).append("'");
            }
        }

        return builder.toString();
    }

    public String combinationToString(boolean usePreparedMark){
        if (!getWhereConditionType().equals(WhereConditionType.COMBINATION)) return null;
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(this.condition1.singleToString(usePreparedMark))
                .append(" ").append(this.combinationOperator.toString())
                .append(" ").append(this.condition2.singleToString(usePreparedMark))
                .append(")");

        return builder.toString();
    }

    public LinkedHashSet<WhereCondition> getConditions(){
        LinkedHashSet<WhereCondition> conditions = new LinkedHashSet<>();
        switch (getWhereConditionType()){
            case SINGLE:
                conditions.add(this);
                break;
            case COMBINATION:
                conditions.addAll(this.condition1.getConditions());
                conditions.addAll(this.condition2.getConditions());
                break;
        }
        return conditions;
    }

    public WhereConditionType getWhereConditionType() {
        return whereConditionType;
    }

    public WhereCondition setUsingNotOperator(boolean usingNotOperator) {
        this.usingNotOperator = usingNotOperator;
        return this;
    }

    public boolean isUsingNotOperator() {
        return usingNotOperator;
    }

    public String toQuery(boolean usePreparedMark) {
        switch (getWhereConditionType()){
            case SINGLE:
                return singleToString(usePreparedMark);
            case COMBINATION:
                return combinationToString(usePreparedMark);
            default:
                return null;
        }
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public ConditionOperator getConditionOperator() {
        return conditionOperator;
    }
}
