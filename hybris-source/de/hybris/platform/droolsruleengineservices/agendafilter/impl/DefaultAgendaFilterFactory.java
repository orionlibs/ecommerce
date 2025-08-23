package de.hybris.platform.droolsruleengineservices.agendafilter.impl;

import de.hybris.platform.droolsruleengineservices.agendafilter.AgendaFilterCreationStrategy;
import de.hybris.platform.droolsruleengineservices.agendafilter.AgendaFilterFactory;
import de.hybris.platform.droolsruleengineservices.agendafilter.CompoundAgendaFilter;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.kie.api.runtime.rule.AgendaFilter;

public class DefaultAgendaFilterFactory implements AgendaFilterFactory
{
    private Class<? extends CompoundAgendaFilter> targetClass;
    private List<AgendaFilterCreationStrategy> strategies;
    private boolean forceAllEvaluations = false;


    public AgendaFilter createAgendaFilter(AbstractRuleEngineContextModel context)
    {
        if(CollectionUtils.isEmpty(getStrategies()))
        {
            return null;
        }
        List<AgendaFilter> agendaFilters = new ArrayList<>();
        for(AgendaFilterCreationStrategy strategy : getStrategies())
        {
            agendaFilters.add(strategy.createAgendaFilter(context));
        }
        CompoundAgendaFilter result = createFromClass();
        result.setAgendaFilters(agendaFilters);
        result.setForceAllEvaluations(isForceAllEvaluations());
        return (AgendaFilter)result;
    }


    protected List<AgendaFilterCreationStrategy> getStrategies()
    {
        return this.strategies;
    }


    public void setStrategies(List<AgendaFilterCreationStrategy> strategies)
    {
        this.strategies = strategies;
    }


    protected boolean isForceAllEvaluations()
    {
        return this.forceAllEvaluations;
    }


    public void setForceAllEvaluations(boolean forceAllEvaluations)
    {
        this.forceAllEvaluations = forceAllEvaluations;
    }


    public void setTargetClass(Class<? extends CompoundAgendaFilter> targetClass)
    {
        this.targetClass = targetClass;
        if(targetClass != null)
        {
            createFromClass();
        }
    }


    protected CompoundAgendaFilter createFromClass()
    {
        try
        {
            return this.targetClass.newInstance();
        }
        catch(InstantiationException | IllegalAccessException e)
        {
            throw new IllegalArgumentException("Cannot instantiate target class, it has no zero arguments constructor:" + this.targetClass
                            .getSimpleName(), e);
        }
    }
}
