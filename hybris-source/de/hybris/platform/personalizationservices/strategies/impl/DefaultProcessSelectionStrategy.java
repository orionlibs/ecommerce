package de.hybris.platform.personalizationservices.strategies.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.strategies.ProcessSelectionStrategy;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

public class DefaultProcessSelectionStrategy implements ProcessSelectionStrategy
{
    private CxConfigurationService cxConfigurationService;


    public String retrieveProcessDefinitionName(UserModel user, Collection<CatalogVersionModel> catalogVersions)
    {
        return this.cxConfigurationService.getCalculationProcessName();
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }
}
