package de.hybris.platform.personalizationcms.action.dao.impl;

import de.hybris.platform.catalog.daos.CatalogDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationcms.dao.CxMulticountryCatalogSupport;
import de.hybris.platform.personalizationcms.model.CxCmsActionModel;
import de.hybris.platform.personalizationservices.action.dao.CxActionTypeDao;
import de.hybris.platform.personalizationservices.dao.CxMulticountryParamSupport;
import de.hybris.platform.personalizationservices.dao.impl.AbstractCxDao;
import de.hybris.platform.personalizationservices.enums.CxActionType;
import de.hybris.platform.personalizationservices.exceptions.EmptyResultParameterCombinationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;

public class CxCmsActionTypeDao extends AbstractCxDao implements CxActionTypeDao<CxCmsActionModel>
{
    protected static final String PAGE_ID_QUERY = "({{SELECT DISTINCT {ac.pk} FROM {CxCmsAction as ac JOIN CxCmsComponentContainer as con ON {ac.containerId} = {con.sourceId} JOIN " + GeneratedCms2Constants.Relations.ELEMENTSFORSLOT
                    + " as rel ON {con.pk} = {rel.target} JOIN ContentSlot as cont ON {rel.source} = {cont.pk} } WHERE   {cont.pk} IN   (    SELECT x.contentSlot FROM     (      ( {{ SELECT {csTempl.contentSlot} as contentSlot         FROM {ContentSlotForTemplate as csTempl         JOIN PageTemplate as pTempl ON {csTempl.pageTemplate} = {pTempl.pk}         JOIN AbstractPage as abstrPage ON {pTempl.pk} = {abstrPage.masterTemplate}        }         WHERE           {abstrPage.catalogVersion} = ?pageCatalogVersion           AND {abstrPage.uid} = ?pageId       }} )      UNION ALL       ( {{ SELECT {csPage.contentSlot} as contentSlot         FROM { ContentSlotForPage as csPage         JOIN AbstractPage as abstrPage ON {csPage.page} = {abstrPage.pk}          }         WHERE           {abstrPage.catalogVersion} = ?pageCatalogVersion           AND {abstrPage.uid} = ?pageId       }} )     ) x   ) }}) ";
    protected static final String CATALOG_VERSION = "catalogVersion";
    protected static final String PAGE_CATALOG_ID = "pageCatalogId";
    protected static final String PAGE_CATALOG_VERSION = "pageCatalogVersion";
    private static final Logger LOGGER = Logger.getLogger(CxCmsActionTypeDao.class);
    private CatalogDao catalogDao;


    public CxCmsActionTypeDao()
    {
        super("CxCmsAction");
    }


    public CxActionType getSupportedType()
    {
        return CxActionType.CXCMSACTION;
    }


    public SearchPageData<CxCmsActionModel> getActions(CatalogVersionModel catalogVersion, Map<String, String> searchCriteria, SearchPageData<?> pagination)
    {
        Map<String, Object> params;
        try
        {
            params = buildParams(catalogVersion, searchCriteria);
        }
        catch(EmptyResultParameterCombinationException e)
        {
            LOGGER.debug("Problem with parameter occurred: ", (Throwable)e);
            return buildEmptySearchPageData(pagination.getPagination());
        }
        String query = buildQuery(params);
        return queryList(query, params, pagination);
    }


    protected Map<String, Object> buildParams(CatalogVersionModel catalogVersion, Map<String, String> searchCriteria)
    {
        Map<String, Object> result = new HashMap<>();
        for(Parameters p : Parameters.values())
        {
            p.addQueryParam(searchCriteria, result);
        }
        result.put("catalogVersion", catalogVersion);
        if(searchCriteria.containsKey(Parameters.PAGE_ID.paramName))
        {
            CatalogVersionModel pageCatalogVersion = getPageCatalogVersion(catalogVersion, searchCriteria);
            result.put("pageCatalogVersion", pageCatalogVersion);
        }
        Map<String, Object> extraCatalogs = CxMulticountryCatalogSupport.createCatalogParams(catalogVersion, searchCriteria);
        result.putAll(extraCatalogs);
        return result;
    }


    protected CatalogVersionModel getPageCatalogVersion(CatalogVersionModel catalogVersion, Map<String, String> params)
    {
        if(params.containsKey("pageCatalogId"))
        {
            String pageCatalogId = params.get("pageCatalogId");
            String catalogId = catalogVersion.getCatalog().getId();
            if(!pageCatalogId.equals(catalogId))
            {
                return this.catalogDao.findCatalogById(pageCatalogId).getActiveCatalogVersion();
            }
        }
        return catalogVersion;
    }


    protected String buildQuery(Map<String, Object> params)
    {
        StringBuilder q = new StringBuilder();
        q.append(" SELECT {a.pk}");
        q.append(" FROM {CxCmsAction AS a");
        q.append(" JOIN CxVariation AS v ON {v.pk} = {a.variation}");
        q.append(" JOIN CxCustomization AS c ON {c.pk} = {v.customization}");
        q.append(" }");
        q.append(" WHERE");
        q.append(" {a.catalogVersion} ").append(CxMulticountryParamSupport.getMulticountryWhereOperator(params));
        Objects.requireNonNull(q);
        Arrays.<Parameters>stream(Parameters.values()).filter(p -> params.containsKey(p.paramName)).map(Parameters::getWhere).forEach(q::append);
        q.append(" ORDER BY").append(CxMulticountryParamSupport.buildOrderByForMulticountry(params, "c"));
        q.append(" {c.groupPOS} ASC,");
        q.append(" {v.customizationPOS} ASC,");
        q.append(" {a.variationPOS} ASC");
        return q.toString();
    }


    private static String likeSupplier(String value)
    {
        return "%" + value + "%";
    }


    protected CatalogDao getCatalogDao()
    {
        return this.catalogDao;
    }


    public void setCatalogDao(CatalogDao catalogDao)
    {
        this.catalogDao = catalogDao;
    }
}
