package de.hybris.platform.personalizationcms.customization.dao.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.personalizationservices.customization.dao.CxCustomizationDaoStrategy;
import de.hybris.platform.personalizationservices.customization.dao.impl.AbstractCxCustomizationDaoStrategy;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class CxCustomizationPageIdDaoStrategy extends AbstractCxCustomizationDaoStrategy implements CxCustomizationDaoStrategy
{
    private static final String CATALOG_VERSION = "catalogVersion";
    private static final String PAGE_ID = "pageId";
    private static final String PAGE_CATALOG_ID = "pageCatalogId";
    private static final String PAGE_CATALOG_VERSION = "pageCatalogVersion";
    private static final String NEGATE_RESULT = "negatePageId";
    private static final Set<String> BASE_PARAMS = Collections.unmodifiableSet(Sets.newHashSet((Object[])new String[] {"pageId", "statuses"}));
    private static final Set<String> EX_PARAMS = Collections.unmodifiableSet(Sets.newHashSet((Object[])new String[] {"pageId", "statuses", "name"}));
    private boolean nameRequired = false;
    private CatalogService catalogService;


    public Set<String> getRequiredParameters()
    {
        return this.nameRequired ? EX_PARAMS : BASE_PARAMS;
    }


    public FlexibleSearchQuery getQuery(Map<String, String> params)
    {
        return getQuery(params, null);
    }


    public FlexibleSearchQuery getQuery(Map<String, String> params, Map<String, Object> queryParams)
    {
        String name = getName(params);
        String statuses = params.get("statuses");
        String query = "SELECT {cu.pk} FROM {CxCustomization as cu } WHERE {cu.pk} " + getOperator(params)
                        + " ({{ SELECT DISTINCT {c.pk} FROM {CxCustomization as c JOIN CxVariation as v ON {c.pk} = {v.customization} JOIN CxCmsAction as a ON {v.pk} = {a.variation} JOIN CxCmsComponentContainer as con ON {a.containerId} = {con.sourceId} JOIN "
                        + GeneratedCms2Constants.Relations.ELEMENTSFORSLOT
                        + " as rel ON {con.pk} = {rel.target} JOIN ContentSlot as cont ON {rel.source} = {cont.pk} } WHERE   {cont.pk} IN   (    SELECT x.contentSlot FROM     (      ( {{ SELECT {csTempl.contentSlot} as contentSlot         FROM {ContentSlotForTemplate as csTempl         JOIN PageTemplate as pTempl ON {csTempl.pageTemplate} = {pTempl.pk}         JOIN AbstractPage as abstrPage ON {pTempl.pk} = {abstrPage.masterTemplate}        }         WHERE           {abstrPage.catalogVersion} = ?pageCatalogVersion           AND {abstrPage.uid} = ?pageId       }} )      UNION ALL       ( {{ SELECT {csPage.contentSlot} as contentSlot         FROM { ContentSlotForPage as csPage         JOIN AbstractPage as abstrPage ON {csPage.page} = {abstrPage.pk}          }         WHERE           {abstrPage.catalogVersion} = ?pageCatalogVersion           AND {abstrPage.uid} = ?pageId       }} )     ) x   )  AND {c.catalogVersion} "
                        + getMulticountryWhereOperator(queryParams) + " }}) AND {cu.catalogVersion} " + getMulticountryWhereOperator(queryParams) + "AND {cu.status} IN (?statuses) AND LOWER({cu.name}) LIKE LOWER(?name) ORDER BY " + buildOrderByForMulticountry(queryParams, "cu")
                        + " {cu.groupPOS} ASC ";
        Map<String, Object> extraParams = new HashMap<>();
        extraParams.put("pageId", params.get("pageId"));
        extraParams.put("name", name);
        extraParams.put("statuses", getStatusesForCodesStr(statuses));
        extraParams.put("pageCatalogVersion", getPageCatalogVersion(params, queryParams));
        return getCxDaoQueryBuilder().buildQuery(query, extraParams);
    }


    protected String getOperator(Map<String, String> params)
    {
        String negate = params.get("negatePageId");
        if(Boolean.parseBoolean(negate))
        {
            return " NOT IN ";
        }
        return " IN ";
    }


    protected String getName(Map<String, String> params)
    {
        if(this.nameRequired)
        {
            return "%" + (String)StringUtils.defaultIfEmpty(params.get("name"), "") + "%";
        }
        return "%";
    }


    protected CatalogVersionModel getPageCatalogVersion(Map<String, String> params, Map<String, Object> queryParams)
    {
        Optional<CatalogVersionModel> catalogVersion = Optional.of((CatalogVersionModel)queryParams.get("catalogVersion"));
        String catalogId = catalogVersion.map(cv -> cv.getCatalog()).map(c -> c.getId()).orElse(null);
        if(params.containsKey("pageCatalogId"))
        {
            CatalogModel pageCatalog = this.catalogService.getCatalogForId(params.get("pageCatalogId"));
            if(pageCatalog != null && !pageCatalog.getId().equals(catalogId))
            {
                return pageCatalog.getActiveCatalogVersion();
            }
        }
        return catalogVersion.get();
    }


    public void setNameRequired(boolean nameRequired)
    {
        this.nameRequired = nameRequired;
    }


    protected CatalogService getCatalogService()
    {
        return this.catalogService;
    }


    @Required
    public void setCatalogService(CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }
}
