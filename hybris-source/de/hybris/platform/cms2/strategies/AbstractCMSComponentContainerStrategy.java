package de.hybris.platform.cms2.strategies;

import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractCMSComponentContainerStrategy implements CMSComponentContainerStrategy
{
    private Class<? extends AbstractCMSComponentContainerModel> containerClass;


    public Class<? extends AbstractCMSComponentContainerModel> getContainerClass()
    {
        return this.containerClass;
    }


    @Required
    public void setContainerClass(Class<? extends AbstractCMSComponentContainerModel> containerClass)
    {
        this.containerClass = containerClass;
    }
}
