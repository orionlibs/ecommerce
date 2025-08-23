package de.hybris.bootstrap.loader.metrics.internal;

import java.util.Objects;

public class RejectionRuleCounterKey
{
    private final String name;
    private final String rule;


    public RejectionRuleCounterKey(String name, String rule)
    {
        this.name = name;
        this.rule = rule;
    }


    public String getName()
    {
        return this.name;
    }


    public String getRule()
    {
        return this.rule;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        RejectionRuleCounterKey that = (RejectionRuleCounterKey)o;
        return (Objects.equals(this.name, that.name) && Objects.equals(this.rule, that.rule));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.name, this.rule});
    }
}
