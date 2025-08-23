package de.hybris.platform.ruleengineservices.compiler;

import java.io.Serializable;
import java.util.Map;

public class RuleIrVariablesContainer implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private RuleIrVariablesContainer parent;
    private Map<String, RuleIrVariablesContainer> children;
    private Map<String, RuleIrVariable> variables;
    private String[] path;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setParent(RuleIrVariablesContainer parent)
    {
        this.parent = parent;
    }


    public RuleIrVariablesContainer getParent()
    {
        return this.parent;
    }


    public void setChildren(Map<String, RuleIrVariablesContainer> children)
    {
        this.children = children;
    }


    public Map<String, RuleIrVariablesContainer> getChildren()
    {
        return this.children;
    }


    public void setVariables(Map<String, RuleIrVariable> variables)
    {
        this.variables = variables;
    }


    public Map<String, RuleIrVariable> getVariables()
    {
        return this.variables;
    }


    public void setPath(String[] path)
    {
        this.path = path;
    }


    public String[] getPath()
    {
        return this.path;
    }
}
