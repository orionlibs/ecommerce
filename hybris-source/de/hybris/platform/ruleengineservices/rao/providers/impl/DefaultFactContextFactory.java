package de.hybris.platform.ruleengineservices.rao.providers.impl;

import de.hybris.platform.ruleengineservices.enums.FactContextType;
import de.hybris.platform.ruleengineservices.rao.providers.FactContextFactory;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFactContextFactory implements FactContextFactory
{
    private Map<String, Map<Class, List<RAOProvider>>> raoProviders;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFactContextFactory.class);


    public FactContext createFactContext(FactContextType type, Collection<?> facts)
    {
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("###################FactContextFactory RaoProviders Map####################");
            this.raoProviders.forEach((k, v) -> LOGGER.debug("Map Key+++++++++++++ {}. Map Value+++++++++++ {}", k, v));
        }
        if(!getRaoProviders().containsKey(type.toString()))
        {
            throw new IllegalArgumentException(String.format("The Fact Context Type with name '%s' is not defined", new Object[] {type.name()}));
        }
        return new FactContext(type, getRaoProviders().get(type.name()), facts);
    }


    protected Map<String, Map<Class, List<RAOProvider>>> getRaoProviders()
    {
        return this.raoProviders;
    }


    @Required
    public void setRaoProviders(Map<String, Map<Class, List<RAOProvider>>> raoProviders)
    {
        this.raoProviders = raoProviders;
    }
}
