package de.hybris.platform.ruleengineservices.rule.data;

import java.io.Serializable;

public class RuleParameterData implements Serializable
{
    private String uuid;
    private String type;
    private Object value;


    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }


    public String getUuid()
    {
        return this.uuid;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getType()
    {
        return this.type;
    }


    public void setValue(Object value)
    {
        this.value = value;
    }


    public <T> T getValue()
    {
        return (T)this.value;
    }
}
