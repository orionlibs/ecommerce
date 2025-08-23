package de.hybris.platform.cms2.multicountry.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.multicountry.service.CatalogLevelService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCatalogLevelService implements CatalogLevelService
{
    private CMSAdminSiteService cmsAdminSiteService;
    private Comparator<ContentSlotModel> cmsItemCatalogLevelComparator;


    public boolean isTopLevel(ContentCatalogModel catalog)
    {
        return Objects.isNull(catalog.getSuperCatalog());
    }


    public boolean isBottomLevel(ContentCatalogModel catalog)
    {
        return CollectionUtils.isEmpty(catalog.getSubCatalogs());
    }


    public boolean isIntermediateLevel(ContentCatalogModel catalog)
    {
        return (Objects.nonNull(catalog.getSuperCatalog()) && CollectionUtils.isNotEmpty(catalog.getSubCatalogs()));
    }


    public int getCatalogLevel(ContentCatalogModel catalog)
    {
        int level = 0;
        if(getCmsAdminSiteService().getActiveSite() != null)
        {
            List<ContentCatalogModel> siteCatalogs = getCmsAdminSiteService().getActiveSite().getContentCatalogs();
            Set<ContentCatalogModel> allSuperCatalogs = getAllSuperCatalogs(Arrays.asList(new ContentCatalogModel[] {catalog}));
            level = allSuperCatalogs.size();
        }
        return level;
    }


    protected Set<ContentCatalogModel> getAllSuperCatalogs(Collection<ContentCatalogModel> contentCatalogs)
    {
        Set<ContentCatalogModel> superCatalogs = (Set<ContentCatalogModel>)contentCatalogs.stream().filter(catalog -> Objects.nonNull(catalog.getSuperCatalog())).map(ContentCatalogModel::getSuperCatalog).collect(Collectors.toSet());
        Set<ContentCatalogModel> results = new HashSet<>();
        while(CollectionUtils.isNotEmpty(superCatalogs))
        {
            superCatalogs.removeIf(catalogModel -> !results.add(catalogModel));
            if(superCatalogs.isEmpty())
            {
                break;
            }
            superCatalogs = getAllSuperCatalogs(superCatalogs);
        }
        return results;
    }


    @Deprecated(since = "2105")
    public Collection<ContentCatalogModel> getAllSuperCatalogs(ContentCatalogModel catalog, CMSSiteModel cmsSiteModel)
    {
        return getAllSuperCatalogs(catalog);
    }


    public Collection<ContentCatalogModel> getAllSuperCatalogs(ContentCatalogModel catalog)
    {
        return getAllSuperCatalogs(Arrays.asList(new ContentCatalogModel[] {catalog}));
    }


    protected Set<ContentCatalogModel> getAllSubCatalogs(Collection<ContentCatalogModel> contentCatalogs)
    {
        Set<ContentCatalogModel> currentLevel = (Set<ContentCatalogModel>)contentCatalogs.stream().map(ContentCatalogModel::getSubCatalogs).filter(CollectionUtils::isNotEmpty).flatMap(Collection::stream).collect(Collectors.toSet());
        Set<ContentCatalogModel> results = new HashSet<>();
        while(!CollectionUtils.isEmpty(currentLevel))
        {
            currentLevel.removeIf(catalogModel -> !results.add(catalogModel));
            if(currentLevel.isEmpty())
            {
                break;
            }
            currentLevel = getAllSubCatalogs(currentLevel);
        }
        return results;
    }


    public Collection<ContentCatalogModel> getAllSubCatalogs(ContentCatalogModel catalog)
    {
        return getAllSubCatalogs(Arrays.asList(new ContentCatalogModel[] {catalog}));
    }


    public List<ContentSlotModel> getSortedMultiCountryContentSlots(List<ContentSlotModel> list)
    {
        return (List<ContentSlotModel>)list.stream().sorted(getCmsItemCatalogLevelComparator()).collect(Collectors.toList());
    }


    public List<CatalogVersionModel> getLevelCatalogVersions(AbstractPageModel page, List<ContentCatalogModel> catalogs)
    {
        List<CatalogVersionModel> catalogVersions = new ArrayList<>();
        CatalogVersionModel contentCatalogVersion = page.getCatalogVersion();
        int level = getCatalogLevel((ContentCatalogModel)contentCatalogVersion.getCatalog());
        Optional.<List<ContentCatalogModel>>ofNullable(catalogs).ifPresent(contentCatalogs -> contentCatalogs.forEach(()));
        return catalogVersions;
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


    protected Comparator<ContentSlotModel> getCmsItemCatalogLevelComparator()
    {
        return this.cmsItemCatalogLevelComparator;
    }


    @Required
    public void setCmsItemCatalogLevelComparator(Comparator<ContentSlotModel> cmsItemCatalogLevelComparator)
    {
        this.cmsItemCatalogLevelComparator = cmsItemCatalogLevelComparator;
    }
}
