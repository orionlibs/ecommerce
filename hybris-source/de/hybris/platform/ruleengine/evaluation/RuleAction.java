package de.hybris.platform.ruleengine.evaluation;

import java.util.Collection;

public interface RuleAction
{
    void insertFacts(Object paramObject, Object... paramVarArgs);


    void insertFacts(Object paramObject, Collection paramCollection);


    void updateFacts(Object paramObject, Object... paramVarArgs);
}
