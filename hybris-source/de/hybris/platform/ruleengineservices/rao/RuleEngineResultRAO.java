package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;

public class RuleEngineResultRAO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Date startTime;
    private Date endTime;
    private LinkedHashSet<AbstractRuleActionRAO> actions;


    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    public Date getStartTime()
    {
        return this.startTime;
    }


    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }


    public Date getEndTime()
    {
        return this.endTime;
    }


    public void setActions(LinkedHashSet<AbstractRuleActionRAO> actions)
    {
        this.actions = actions;
    }


    public LinkedHashSet<AbstractRuleActionRAO> getActions()
    {
        return this.actions;
    }
}
