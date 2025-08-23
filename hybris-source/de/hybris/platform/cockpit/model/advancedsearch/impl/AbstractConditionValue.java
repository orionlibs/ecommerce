package de.hybris.platform.cockpit.model.advancedsearch.impl;

import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import de.hybris.platform.cockpit.model.search.Operator;

public abstract class AbstractConditionValue implements ConditionValue
{
    private boolean linkedByOR = false;
    protected Operator operator;


    public Operator getOperator()
    {
        return this.operator;
    }


    public boolean isLinkedByOR()
    {
        return this.linkedByOR;
    }


    public void setLinkedByOR(boolean linkedByOR)
    {
        this.linkedByOR = linkedByOR;
    }
}
