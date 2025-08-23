package de.hybris.platform.persistence.polyglot.search.criteria;

public interface ConditionVisitor
{
    default void accept(Conditions.LogicalAndCondition condition)
    {
    }


    default void accept(Conditions.LogicalOrCondition condition)
    {
    }


    default void accept(Conditions.ComparisonCondition condition)
    {
    }
}
