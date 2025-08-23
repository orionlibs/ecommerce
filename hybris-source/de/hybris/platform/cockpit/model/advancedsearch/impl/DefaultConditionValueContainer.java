package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import java.util.Set;

public class DefaultConditionValueContainer extends AbstractConditionValueContainer
{
    public void setConditionValues(Set<ConditionValue> values)
    {
        this.conditionValues = values;
    }
}
