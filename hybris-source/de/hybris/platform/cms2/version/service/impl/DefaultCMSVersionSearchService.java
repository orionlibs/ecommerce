package de.hybris.platform.cms2.version.service.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.common.exceptions.PermissionExceptionUtils;
import de.hybris.platform.cms2.common.service.SearchHelper;
import de.hybris.platform.cms2.data.CMSVersionSearchData;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.enums.SortDirection;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.namedquery.NamedQuery;
import de.hybris.platform.cms2.namedquery.Sort;
import de.hybris.platform.cms2.namedquery.service.NamedQueryService;
import de.hybris.platform.cms2.version.service.CMSVersionSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSVersionSearchService implements CMSVersionSearchService
{
    protected static final String MASK_QUERY_PARAM = "mask";
    protected static final String NAMED_QUERY_CMS_VERSION_SEARCH_BY_ITEM_UID = "namedQueryCMSVersionByItemUid";
    protected static final String NAMED_QUERY_CMS_VERSION_SEARCH_BY_ITEM_UID_WITH_LABEL = "namedQueryCMSVersionByItemUidWithLabel";
    private CatalogVersionService catalogVersionService;
    private NamedQueryService namedQueryService;
    private SearchHelper searchHelper;
    private PermissionCRUDService permissionCRUDService;


    public SearchResult<CMSVersionModel> findVersions(CMSVersionSearchData cmsVersionSearchData, PageableData pageableData)
    {
        ServicesUtil.validateParameterNotNull(cmsVersionSearchData, "CMSVersionSearchData object cannot be null.");
        ServicesUtil.validateParameterNotNull(pageableData, "PageableData object cannot be null.");
        if(!getPermissionCRUDService().canReadType("CMSVersion"))
        {
            throw PermissionExceptionUtils.createTypePermissionException("read", "CMSVersion");
        }
        CatalogVersionModel itemCatalogVersionModel = getCatalogVersionService().getCatalogVersion(cmsVersionSearchData.getItemCatalogId(), cmsVersionSearchData.getItemCatalogVersion());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("itemUid", cmsVersionSearchData.getItemUid());
        parameters.put("itemCatalogVersion", itemCatalogVersionModel);
        if(StringUtils.isNotBlank(cmsVersionSearchData.getMask()))
        {
            parameters.put("mask", "%" + cmsVersionSearchData.getMask() + "%");
        }
        if(StringUtils.isBlank(pageableData.getSort()))
        {
            pageableData.setSort("creationtime");
        }
        List<Sort> sortList = getSearchHelper().convertSort(pageableData.getSort(), SortDirection.DESC);
        String queryName = StringUtils.isNotBlank(cmsVersionSearchData.getMask()) ? "namedQueryCMSVersionByItemUidWithLabel" : "namedQueryCMSVersionByItemUid";
        NamedQuery namedQuery = (new NamedQuery()).withQueryName(queryName).withCurrentPage(Integer.valueOf(pageableData.getCurrentPage())).withPageSize(Integer.valueOf(pageableData.getPageSize())).withParameters(parameters).withSort(sortList);
        return getNamedQueryService().getSearchResult(namedQuery);
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected NamedQueryService getNamedQueryService()
    {
        return this.namedQueryService;
    }


    @Required
    public void setNamedQueryService(NamedQueryService namedQueryService)
    {
        this.namedQueryService = namedQueryService;
    }


    protected SearchHelper getSearchHelper()
    {
        return this.searchHelper;
    }


    @Required
    public void setSearchHelper(SearchHelper searchHelper)
    {
        this.searchHelper = searchHelper;
    }


    protected PermissionCRUDService getPermissionCRUDService()
    {
        return this.permissionCRUDService;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }
}
