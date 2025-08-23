package de.hybris.platform.personalizationservices.process.strategies.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.personalizationservices.model.process.CxPersonalizationProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.site.BaseSiteService;
import org.springframework.beans.factory.annotation.Required;

public class CxProcessParameterBaseSiteStrategy extends AbstractCxProcessParameterStrategy
{
    private BaseSiteService baseSiteService;


    public void load(CxPersonalizationProcessModel process)
    {
        consumeProcessParameter(process, "baseSiteUid", baseSiteUid -> this.baseSiteService.setCurrentBaseSite(baseSiteUid, false));
    }


    public void store(CxPersonalizationProcessModel process)
    {
        BaseSiteModel baseSite = this.baseSiteService.getCurrentBaseSite();
        if(baseSite != null)
        {
            getProcessParameterHelper().setProcessParameter((BusinessProcessModel)process, "baseSiteUid", baseSite.getUid());
        }
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }


    protected BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }
}
