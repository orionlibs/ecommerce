package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.LinkedHashSet;

public class AbstractActionedRAO implements Serializable
{
    private LinkedHashSet<AbstractRuleActionRAO> actions;


    public void setActions(LinkedHashSet<AbstractRuleActionRAO> actions)
    {
        this.actions = actions;
    }


    public LinkedHashSet<AbstractRuleActionRAO> getActions()
    {
        return this.actions;
    }
}
