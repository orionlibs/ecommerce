package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import de.hybris.platform.cockpit.model.advancedsearch.ConditionValueContainer;
import java.util.Collections;
import java.util.Set;

public abstract class AbstractConditionValueContainer implements ConditionValueContainer
{
    protected Set<ConditionValue> conditionValues = null;


    public Set<ConditionValue> getConditionValues()
    {
        return (this.conditionValues == null) ? Collections.EMPTY_SET : this.conditionValues;
    }
}
