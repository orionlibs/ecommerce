package de.hybris.platform.ruleengineservices.compiler;

import java.io.Serializable;
import java.util.Map;

public class RuleIrLocalVariablesContainer implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<String, RuleIrVariable> variables;


    public void setVariables(Map<String, RuleIrVariable> variables)
    {
        this.variables = variables;
    }


    public Map<String, RuleIrVariable> getVariables()
    {
        return this.variables;
    }
}
