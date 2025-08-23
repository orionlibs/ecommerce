package de.hybris.platform.cmscockpit.cms.strategies.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.cms.strategies.CounterpartProductCatalogVersionsStrategy;
import de.hybris.platform.site.BaseSiteService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCounterpartProductCatalogVersionsStrategy implements CounterpartProductCatalogVersionsStrategy
{
    private CMSAdminSiteService cmsAdminSiteService;
    private BaseSiteService baseSiteService;


    public Collection<CatalogVersionModel> getCounterpartProductCatalogVersions()
    {
        CMSSiteModel cmsSite = getCmsAdminSiteService().getActiveSite();
        CatalogVersionModel currentCatalogVersion = getCmsAdminSiteService().getActiveCatalogVersion();
        if(currentCatalogVersion.getCatalog() instanceof de.hybris.platform.cms2.model.contents.ContentCatalogModel)
        {
            String versionStringToMatch = currentCatalogVersion.getVersion();
            List<CatalogModel> productCatalogs = getBaseSiteService().getProductCatalogs((BaseSiteModel)cmsSite);
            Set<CatalogVersionModel> applicableCatalogVersions = new HashSet<>();
            for(CatalogModel catalog : productCatalogs)
            {
                for(CatalogVersionModel catalogVersion : catalog.getCatalogVersions())
                {
                    if(catalogVersion.getVersion().equals(versionStringToMatch))
                    {
                        applicableCatalogVersions.add(catalogVersion);
                    }
                }
            }
            if(CollectionUtils.isEmpty(applicableCatalogVersions))
            {
                for(CatalogModel catalog : productCatalogs)
                {
                    applicableCatalogVersions.add(catalog.getActiveCatalogVersion());
                }
            }
            return applicableCatalogVersions;
        }
        return Collections.singleton(currentCatalogVersion);
    }


    public CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.cmsAdminSiteService;
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService cmsAdminSiteService)
    {
        this.cmsAdminSiteService = cmsAdminSiteService;
    }


    public BaseSiteService getBaseSiteService()
    {
        return this.baseSiteService;
    }


    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }
}
