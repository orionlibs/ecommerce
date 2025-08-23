package de.hybris.platform.droolsruleengineservices.agendafilter.impl;

import de.hybris.platform.droolsruleengineservices.agendafilter.CompoundAgendaFilter;
import java.util.ArrayList;
import java.util.List;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

public class DefaultCompoundAgendaFilter implements CompoundAgendaFilter
{
    private List<AgendaFilter> agendaFilters;
    private boolean forceAllEvaluations = false;


    public DefaultCompoundAgendaFilter()
    {
        this.agendaFilters = new ArrayList<>();
    }


    public boolean accept(Match match)
    {
        boolean result = true;
        for(AgendaFilter agendaFilter : getAgendaFilters())
        {
            result &= agendaFilter.accept(match);
            if(!result && !isForceAllEvaluations())
            {
                break;
            }
        }
        return result;
    }


    protected List<AgendaFilter> getAgendaFilters()
    {
        return this.agendaFilters;
    }


    protected boolean isForceAllEvaluations()
    {
        return this.forceAllEvaluations;
    }


    public void setForceAllEvaluations(boolean forceAllEvaluations)
    {
        this.forceAllEvaluations = forceAllEvaluations;
    }


    public void setAgendaFilters(List<AgendaFilter> agendaFilters)
    {
        this.agendaFilters = agendaFilters;
    }


    public void addAgendaFilter(AgendaFilter filter)
    {
        this.agendaFilters.add(filter);
    }
}
