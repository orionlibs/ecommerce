package de.hybris.platform.persistence.polyglot.search.criteria;

import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import java.util.Arrays;
import java.util.List;

public final class Conditions
{
    public static ComparisonCondition cmp(SingleAttributeKey key, ComparisonCondition.CmpOperator operator, String paramNameToCompare)
    {
        return new ComparisonCondition(key, operator, paramNameToCompare);
    }


    public static LogicalAndCondition and(Condition... conditions)
    {
        return and(Arrays.asList(conditions));
    }


    public static LogicalAndCondition and(List<Condition> conditions)
    {
        return new LogicalAndCondition(conditions);
    }


    public static LogicalOrCondition or(Condition... conditions)
    {
        return or(Arrays.asList(conditions));
    }


    public static LogicalOrCondition or(List<Condition> conditions)
    {
        return new LogicalOrCondition(conditions);
    }
}
