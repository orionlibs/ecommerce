package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import java.util.Collections;

public class SingleConditionValueContainer extends AbstractConditionValueContainer
{
    public SingleConditionValueContainer(ConditionValue conditionValue)
    {
        this.conditionValues = Collections.singleton(conditionValue);
    }
}
