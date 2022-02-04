package io.github.tivecs.wanderer.storage.sql.utils;

import java.util.LinkedHashSet;

public class WhereBuilder {

    private final LinkedHashSet<WhereCondition> conditions = new LinkedHashSet<>();

    public WhereBuilder(){}

    public WhereCondition create(String column, WhereCondition.ConditionOperator operator, Object value){
        return new WhereCondition(column, operator, value);
    }

    public WhereCondition combine(WhereCondition condition1, WhereCondition.CombinationOperator operator, WhereCondition condition2){
        return new WhereCondition(condition1, operator, condition2);
    }

    public WhereBuilder add(String column1, WhereCondition.ConditionOperator operator1, Object value1, String column2, WhereCondition.ConditionOperator operator2, Object value2, WhereCondition.CombinationOperator combination){
        WhereCondition condition1, condition2;
        condition1 = create(column1, operator1, value1);
        condition2 = create(column2, operator2, value2);
        return add(combine(condition1, combination, condition2));
    }

    public WhereBuilder add(String column, WhereCondition.ConditionOperator operator, Object value){
        return add(create(column, operator, value));
    }

    public WhereBuilder add(WhereCondition condition){
        getConditions().add(condition);
        return this;
    }

    public String translateToQuery(boolean usePreparedMark){
        StringBuilder builder = new StringBuilder();
        for (WhereCondition condition : getConditions()){
            builder.append(condition.toQuery(usePreparedMark));
        }
        return builder.toString();
    }

    public LinkedHashSet<WhereCondition> getConditions() {
        return conditions;
    }
}
