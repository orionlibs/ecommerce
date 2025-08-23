package de.hybris.platform.persistence.polyglot.search.criteria;

public interface Condition
{
    void visit(ConditionVisitor paramConditionVisitor);
}
