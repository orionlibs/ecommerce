package de.hybris.platform.cmscockpit.wizard.strategies;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.services.GenericRandomNameProducer;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.wizards.generic.strategies.PredefinedValuesStrategy;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCmsItemPredefinedValuesStrategy implements PredefinedValuesStrategy
{
    private CMSAdminSiteService cmsAdminSiteService;
    private GenericRandomNameProducer genericRandomNameProducer;


    public Map<String, Object> getPredefinedValues(CreateContext ctx)
    {
        Map<String, Object> ret = new HashMap<>();
        ret.put("CMSItem.catalogVersion", getCmsAdminSiteService().getActiveCatalogVersion());
        ret.put("CMSItem.uid", this.genericRandomNameProducer
                        .generateSequence("CMSItem", "item"));
        return ret;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.cmsAdminSiteService;
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService cmsAdminSiteService)
    {
        this.cmsAdminSiteService = cmsAdminSiteService;
    }


    protected GenericRandomNameProducer getGenericRandomNameProducer()
    {
        return this.genericRandomNameProducer;
    }


    @Required
    public void setGenericRandomNameProducer(GenericRandomNameProducer genericRandomNameProducer)
    {
        this.genericRandomNameProducer = genericRandomNameProducer;
    }
}
