package de.hybris.platform.personalizationcms.container.dao.impl;

import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.personalizationcms.container.dao.CxContainerDao;
import de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxContainerDao implements CxContainerDao
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCxContainerDao.class);
    private FlexibleSearchService flexibleSearchService;


    public List<CxCmsComponentContainerModel> getCxContainersByDefaultComponent(SimpleCMSComponentModel component)
    {
        if(component == null || component.getPk() == null)
        {
            return Collections.emptyList();
        }
        try
        {
            CxCmsComponentContainerModel example = new CxCmsComponentContainerModel();
            example.setCatalogVersion(component.getCatalogVersion());
            example.setDefaultCmsComponent(component);
            return getFlexibleSearchService().getModelsByExample(example);
        }
        catch(FlexibleSearchException e)
        {
            LOG.debug("Search for related containers failed", (Throwable)e);
            return Collections.emptyList();
        }
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
