package de.hybris.platform.warehousing.sourcing.strategy.impl;

import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.sourcing.strategy.SourcingStrategy;
import de.hybris.platform.warehousing.sourcing.strategy.SourcingStrategyMapper;
import de.hybris.platform.warehousing.sourcing.strategy.SourcingStrategyService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSourcingStrategyService implements SourcingStrategyService, InitializingBean
{
    private List<SourcingStrategy> defaultStrategies;


    public List<SourcingStrategy> getStrategies(SourcingContext context, Collection<SourcingStrategyMapper> mappers)
    {
        List<SourcingStrategy> strategies = new ArrayList<>();
        for(SourcingStrategyMapper mapper : mappers)
        {
            if(mapper.isMatch(context).booleanValue())
            {
                strategies.add(mapper.getStrategy());
            }
        }
        return strategies;
    }


    public List<SourcingStrategy> getDefaultStrategies()
    {
        return this.defaultStrategies;
    }


    public void afterPropertiesSet() throws Exception
    {
        if(CollectionUtils.isEmpty(this.defaultStrategies))
        {
            throw new IllegalArgumentException("Default strategies cannot be empty.");
        }
    }


    @Required
    public void setDefaultStrategies(List<SourcingStrategy> defaultStrategies)
    {
        this.defaultStrategies = defaultStrategies;
    }
}
