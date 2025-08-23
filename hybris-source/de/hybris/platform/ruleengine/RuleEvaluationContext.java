package de.hybris.platform.ruleengine;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class RuleEvaluationContext implements Serializable
{
    private static final long serialVersionUID = 1L;
    private AbstractRuleEngineContextModel ruleEngineContext;
    private Set<Object> facts;
    private Map<String, Object> globals;
    private Object filter;
    private Set<Object> eventListeners;


    public void setRuleEngineContext(AbstractRuleEngineContextModel ruleEngineContext)
    {
        this.ruleEngineContext = ruleEngineContext;
    }


    public AbstractRuleEngineContextModel getRuleEngineContext()
    {
        return this.ruleEngineContext;
    }


    public void setFacts(Set<Object> facts)
    {
        this.facts = facts;
    }


    public Set<Object> getFacts()
    {
        return this.facts;
    }


    public void setGlobals(Map<String, Object> globals)
    {
        this.globals = globals;
    }


    public Map<String, Object> getGlobals()
    {
        return this.globals;
    }


    public void setFilter(Object filter)
    {
        this.filter = filter;
    }


    public Object getFilter()
    {
        return this.filter;
    }


    public void setEventListeners(Set<Object> eventListeners)
    {
        this.eventListeners = eventListeners;
    }


    public Set<Object> getEventListeners()
    {
        return this.eventListeners;
    }
}
