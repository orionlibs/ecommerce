package de.hybris.platform.cmscockpit.services.impl;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.services.search.impl.GenericQuerySearchProvider;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Operator;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class CmsSearchProvider extends GenericQuerySearchProvider
{
    private CatalogTypeService catalogTypeService;
    private CMSAdminSiteService siteService;


    protected GenericCondition createCatalogCondition()
    {
        GenericCondition ret = null;
        CatalogVersionModel catVersion = getCmsAdminSiteService().getActiveCatalogVersion();
        if(catVersion != null)
        {
            ret = GenericCondition.createConditionForValueComparison(new GenericSearchField("catalogVersion"), Operator.EQUAL, catVersion
                            .getPk());
        }
        return ret;
    }


    protected GenericCondition createCatalogCondition(CatalogVersionModel catalogVersionModel)
    {
        GenericCondition ret = null;
        if(catalogVersionModel != null)
        {
            ret = GenericCondition.createConditionForValueComparison(new GenericSearchField("catalogVersion"), Operator.EQUAL, catalogVersionModel
                            .getPk());
        }
        return ret;
    }


    public List<GenericCondition> createConditions(Query query, GenericQuery genQuery)
    {
        List<GenericCondition> conditions = new ArrayList<>();
        conditions.addAll(super.createConditions(query, genQuery));
        boolean isCatalogItem = this.catalogTypeService.isCatalogVersionAwareType(getDefaultRootType().getCode());
        if(isCatalogItem)
        {
            GenericCondition specyficCondition = createCatalogCondition();
            if(specyficCondition == null)
            {
                addCondition(createSiteCondition(), conditions);
            }
            else
            {
                addCondition(createCatalogCondition(), conditions);
            }
        }
        return conditions;
    }


    protected GenericCondition createSiteCondition()
    {
        GenericConditionList genericConditionList;
        GenericCondition ret = null;
        CMSSiteModel siteModel = getCmsAdminSiteService().getActiveSite();
        if(siteModel != null)
        {
            List<GenericCondition> conditions = new ArrayList<>();
            for(CatalogModel catalogModel : siteModel.getContentCatalogs())
            {
                for(CatalogVersionModel catalogVersionModel : catalogModel.getCatalogVersions())
                {
                    conditions.add(createCatalogCondition(catalogVersionModel));
                }
            }
            if(!conditions.isEmpty())
            {
                genericConditionList = GenericCondition.createConditionList(conditions, Operator.OR);
            }
        }
        return (GenericCondition)genericConditionList;
    }


    public CatalogTypeService getCatalogTypeService()
    {
        return this.catalogTypeService;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.siteService;
    }


    @Required
    public void setCatalogTypeService(CatalogTypeService catalogTypeService)
    {
        this.catalogTypeService = catalogTypeService;
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService siteService)
    {
        this.siteService = siteService;
    }
}
