package de.hybris.platform.cms2.registry.impl;

import de.hybris.platform.cms2.registry.CMSComponentContainerRegistry;
import de.hybris.platform.cms2.strategies.CMSComponentContainerStrategy;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSComponentContainerRegistry implements CMSComponentContainerRegistry
{
    private Map<String, CMSComponentContainerStrategy> strategies;
    private CMSComponentContainerStrategy defaultCMSComponentContainerStrategy;
    private TypeService typeService;


    public <T extends de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel> CMSComponentContainerStrategy getStrategy(T container)
    {
        String typeCode = getTypeService().getComposedTypeForClass(container.getClass()).getCode();
        Optional<CMSComponentContainerStrategy> containerStrategy = getStrategies().entrySet().stream().filter(entry -> typeCode.equals(entry.getKey())).map(Map.Entry::getValue).findFirst();
        return containerStrategy.orElse(getDefaultCMSComponentContainerStrategy());
    }


    protected Map<String, CMSComponentContainerStrategy> getStrategies()
    {
        return this.strategies;
    }


    @Required
    public void setStrategies(Map<String, CMSComponentContainerStrategy> strategies)
    {
        this.strategies = strategies;
    }


    protected CMSComponentContainerStrategy getDefaultCMSComponentContainerStrategy()
    {
        return this.defaultCMSComponentContainerStrategy;
    }


    @Required
    public void setDefaultCMSComponentContainerStrategy(CMSComponentContainerStrategy defaultCMSComponentContainerStrategy)
    {
        this.defaultCMSComponentContainerStrategy = defaultCMSComponentContainerStrategy;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
