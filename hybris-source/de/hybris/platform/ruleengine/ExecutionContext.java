package de.hybris.platform.ruleengine;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class ExecutionContext implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<String, Long> ruleVersions;
    private InitializeMode initializeMode;
    private Collection<String> modifiedRuleCodes;


    public void setRuleVersions(Map<String, Long> ruleVersions)
    {
        this.ruleVersions = ruleVersions;
    }


    public Map<String, Long> getRuleVersions()
    {
        return this.ruleVersions;
    }


    public void setInitializeMode(InitializeMode initializeMode)
    {
        this.initializeMode = initializeMode;
    }


    public InitializeMode getInitializeMode()
    {
        return this.initializeMode;
    }


    public void setModifiedRuleCodes(Collection<String> modifiedRuleCodes)
    {
        this.modifiedRuleCodes = modifiedRuleCodes;
    }


    public Collection<String> getModifiedRuleCodes()
    {
        return this.modifiedRuleCodes;
    }
}
