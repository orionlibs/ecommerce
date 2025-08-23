package de.hybris.platform.servicelayer.action.impl;

import de.hybris.platform.servicelayer.action.ActionException;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultActionExecutionStrategyRegistry implements ActionExecutionStrategyRegistry
{
    private static final Logger LOG = Logger.getLogger(ActionExecutionStrategyRegistry.class);
    private Map<ActionType, ActionExecutionStrategy> strategiesMap;


    public Set<ActionExecutionStrategy> getAllStrategies()
    {
        return new HashSet<>(this.strategiesMap.values());
    }


    public ActionExecutionStrategy getExecutionStrategy(ActionType type)
    {
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' was null");
        if(this.strategiesMap == null)
        {
            throw new IllegalStateException("strategies not loaded yet");
        }
        ActionExecutionStrategy strat = this.strategiesMap.get(type);
        if(strat == null)
        {
            throw new ActionException("no execution strategy for type " + type + " - got " + this.strategiesMap.keySet());
        }
        return strat;
    }


    @Autowired(required = true)
    public void setStrategies(Collection<ActionExecutionStrategy> strategies)
    {
        Map<ActionType, ActionExecutionStrategy> tempMap = new HashMap<>();
        for(ActionExecutionStrategy strat : strategies)
        {
            for(ActionType type : strat.getAcceptedTypes())
            {
                ActionExecutionStrategy previous = tempMap.get(type);
                if(previous != null)
                {
                    LOG.error("canot register strategy " + strat + " for type " + type + " since it is already mapped to " + previous + " - skipped");
                    continue;
                }
                tempMap.put(type, strat);
            }
        }
        this.strategiesMap = Collections.unmodifiableMap(tempMap);
    }
}
