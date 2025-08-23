package de.hybris.platform.personalizationcms.strategy;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.personalizationservices.enums.CxCatalogLookupType;
import de.hybris.platform.personalizationservices.service.CxCatalogService;
import de.hybris.platform.personalizationservices.strategies.CxCatalogLookupStrategy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class LeavesOrClosestAncestorsCatalogLookupStrategy implements CxCatalogLookupStrategy
{
    private CxCatalogService cxCatalogService;
    private CatalogVersionService catalogVersionService;
    private int maxRecursions = 0;


    public CxCatalogLookupType getType()
    {
        return (this.maxRecursions > 0) ? CxCatalogLookupType.LEAF_CLOSEST_ANCESTOR_CATALOGS : CxCatalogLookupType.LEAF_CATALOGS;
    }


    public List<CatalogVersionModel> getCatalogVersionsForCalculation()
    {
        Collection<CatalogModel> sessionCatalogs = getSessionCatalogs();
        return getPersonalizationCvsFromLeaves(sessionCatalogs, 0);
    }


    protected List<CatalogModel> getSessionCatalogs()
    {
        return (List<CatalogModel>)this.catalogVersionService.getSessionCatalogVersions().stream()
                        .map(cv -> cv.getCatalog())
                        .collect(Collectors.toList());
    }


    protected List<CatalogVersionModel> getPersonalizationCvsFromLeaves(Collection<CatalogModel> sessionCatalogs, int recursionLevel)
    {
        if(recursionLevel > this.maxRecursions || sessionCatalogs.isEmpty())
        {
            return Collections.emptyList();
        }
        Set<CatalogModel> leafCatalogs = getLeafCatalogs(sessionCatalogs);
        List<CatalogVersionModel> personalizationCvs = getCatalogVersionsFromLeafCatalogs(this.catalogVersionService
                        .getSessionCatalogVersions(), leafCatalogs);
        if(!personalizationCvs.isEmpty())
        {
            return personalizationCvs;
        }
        sessionCatalogs.removeAll(leafCatalogs);
        return getPersonalizationCvsFromLeaves(sessionCatalogs, recursionLevel + 1);
    }


    protected Set<CatalogModel> getLeafCatalogs(Collection<CatalogModel> sessionCatalogs)
    {
        return (Set<CatalogModel>)sessionCatalogs.stream()
                        .filter(c -> isLeafCatalog(c, sessionCatalogs))
                        .collect(Collectors.toSet());
    }


    protected List<CatalogVersionModel> getCatalogVersionsFromLeafCatalogs(Collection<CatalogVersionModel> allCatalogsVersions, Set<CatalogModel> leafCatalogs)
    {
        Objects.requireNonNull(this.cxCatalogService);
        return (List<CatalogVersionModel>)allCatalogsVersions.stream().filter(this.cxCatalogService::isPersonalizationInCatalog)
                        .filter(cv -> leafCatalogs.contains(cv.getCatalog()))
                        .collect(Collectors.toList());
    }


    protected boolean isLeafCatalog(CatalogModel catalog, Collection<CatalogModel> sessionCatalogs)
    {
        if(!(catalog instanceof ContentCatalogModel))
        {
            return true;
        }
        ContentCatalogModel contentCatalogModel = (ContentCatalogModel)catalog;
        return (CollectionUtils.isEmpty(contentCatalogModel.getSubCatalogs()) ||
                        !CollectionUtils.containsAny(sessionCatalogs, contentCatalogModel.getSubCatalogs()));
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCxCatalogService(CxCatalogService cxCatalogService)
    {
        this.cxCatalogService = cxCatalogService;
    }


    protected CxCatalogService getCxCatalogService()
    {
        return this.cxCatalogService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected int getMaxRecursions()
    {
        return this.maxRecursions;
    }


    public void setMaxRecursions(int maxRecursions)
    {
        this.maxRecursions = maxRecursions;
    }
}
