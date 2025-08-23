package de.hybris.platform.droolsruleengineservices.agendafilter;

import java.util.List;
import org.kie.api.runtime.rule.AgendaFilter;

public interface CompoundAgendaFilter extends AgendaFilter
{
    void setForceAllEvaluations(boolean paramBoolean);


    void setAgendaFilters(List<AgendaFilter> paramList);


    void addAgendaFilter(AgendaFilter paramAgendaFilter);
}
