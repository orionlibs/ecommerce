package de.hybris.platform.ruleengineservices.compiler;

import java.io.Serializable;
import java.util.Map;

public abstract class RuleIrCondition implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<String, Object> metadata;


    public void setMetadata(Map<String, Object> metadata)
    {
        this.metadata = metadata;
    }


    public Map<String, Object> getMetadata()
    {
        return this.metadata;
    }
}
