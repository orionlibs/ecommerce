package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.compiler.RuleIrProcessor;

public class RuleIrProcessorDefinition implements Comparable<RuleIrProcessorDefinition>
{
    private int priority;
    private RuleIrProcessor processor;


    public int getPriority()
    {
        return this.priority;
    }


    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    public RuleIrProcessor getRuleIrProcessor()
    {
        return this.processor;
    }


    public void setRuleIrProcessor(RuleIrProcessor processor)
    {
        this.processor = processor;
    }


    public int compareTo(RuleIrProcessorDefinition other)
    {
        return Integer.compare(other.getPriority(), getPriority());
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(!o.getClass().equals(RuleIrProcessorDefinition.class))
        {
            return false;
        }
        RuleIrProcessorDefinition that = (RuleIrProcessorDefinition)o;
        if(getPriority() != that.getPriority())
        {
            return false;
        }
        return (getRuleIrProcessor() != null) ?
                        getRuleIrProcessor().equals(that.getRuleIrProcessor()) : (
                        (that.getRuleIrProcessor() == null));
    }


    public int hashCode()
    {
        return getPriority() + ((getRuleIrProcessor() == null) ? 0 : getRuleIrProcessor().hashCode());
    }
}
