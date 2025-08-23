package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.CMSRelationModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentSlotDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.FlexibleSearchUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DefaultCMSContenSlotDao extends AbstractItemDao implements CMSContentSlotDao
{
    protected static final String CATALOG_VERSIONS_QUERY_PARAM = "catalogVersions";
    protected static final String CONTENT_SLOT_UIDS_QUERY_PARAM = "uids";
    protected static final String QUERY_CONTENT_SLOT = "SELECT {pk} FROM {ContentSlotForTemplate} WHERE {contentSlot} =?contentSlot AND {catalogVersion} =?catalogVersion";


    public Collection<AbstractPageModel> findPagesByContentSlot(ContentSlotModel contentSlot)
    {
        String query = "SELECT {page} FROM {ContentSlotForPage} WHERE {contentSlot} = ?slot";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {page} FROM {ContentSlotForPage} WHERE {contentSlot} = ?slot");
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("slot", contentSlot);
        fQuery.addQueryParameters(queryParameters);
        SearchResult<AbstractPageModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForTemplateModel> findAllContentSlotRelationsByPageTemplate(PageTemplateModel template, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {ContentSlotForTemplate} WHERE {pageTemplate} =?pageTemplate AND {catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForTemplate} WHERE {pageTemplate} =?pageTemplate AND {catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("pageTemplate", template);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotForTemplateModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForTemplateModel> findContentSlotRelationsByPageTemplateAndContentSlot(PageTemplateModel template, ContentSlotModel contentSlotModel, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {ContentSlotForTemplate} WHERE      {pageTemplate} =?pageTemplate AND {catalogVersion} =?catalogVersion AND {contentSlot} =?contentSlot";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForTemplate} WHERE      {pageTemplate} =?pageTemplate AND {catalogVersion} =?catalogVersion AND {contentSlot} =?contentSlot");
        Map<String, Object> params = new HashMap<>();
        params.put("pageTemplate", template);
        params.put("catalogVersion", catalogVersion);
        params.put("contentSlot", contentSlotModel);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotForTemplateModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForTemplateModel> findAllContentSlotRelationsByPageTemplate(PageTemplateModel template)
    {
        String query = "SELECT {pk} FROM {ContentSlotForTemplate} WHERE {pageTemplate} =?pageTemplate";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForTemplate} WHERE {pageTemplate} =?pageTemplate");
        fQuery.addQueryParameter("pageTemplate", template);
        SearchResult<ContentSlotForTemplateModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForTemplateModel> findContentSlotRelationsByPageTemplateAndPosition(PageTemplateModel template, String position, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {pk} FROM {ContentSlotForTemplate} WHERE {pageTemplate} =?pageTemplate AND {position} =?position AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("pageTemplate", template);
        queryParameters.put("position", position);
        SearchResult<ContentSlotForTemplateModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<ContentSlotForTemplateModel> findContentSlotRelationsByPageTemplateAndCatalogVersions(PageTemplateModel template, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {pk} FROM {ContentSlotForTemplate}  WHERE {pageTemplate} =?pageTemplate AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("pageTemplate", template);
        SearchResult<ContentSlotForTemplateModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<ContentSlotForTemplateModel> findContentSlotRelationsByPageTemplateAndCatalogVersionsAndContentSlot(PageTemplateModel template, ContentSlotModel contentSlot, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {pk} FROM {ContentSlotForTemplate} WHERE {pageTemplate} =?pageTemplate AND {contentSlot} =?contentSlot AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("pageTemplate", template);
        queryParameters.put("contentSlot", contentSlot);
        SearchResult<ContentSlotForTemplateModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<ContentSlotForPageModel> findAllContentSlotRelationsByPage(AbstractPageModel page, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {ContentSlotForPage} WHERE {page} =?page AND {catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForPage} WHERE {page} =?page AND {catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotForPageModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForPageModel> findAllContentSlotRelationsByPageUid(String pageUid, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {relation.pk}  FROM  {ContentSlotForPage AS relation    JOIN AbstractPage AS page     ON {page.pk} = {relation.page}}  WHERE {page.uid} =?uid  AND  {relation.catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {relation.pk}  FROM  {ContentSlotForPage AS relation    JOIN AbstractPage AS page     ON {page.pk} = {relation.page}}  WHERE {page.uid} =?uid  AND  {relation.catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("uid", pageUid);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotForPageModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForPageModel> findAllContentSlotRelationsByPage(AbstractPageModel page)
    {
        String query = "SELECT {pk} FROM {ContentSlotForPage} WHERE {page} =?page";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForPage} WHERE {page} =?page");
        fQuery.addQueryParameter("page", page);
        SearchResult<ContentSlotForPageModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForPageModel> findAllContentSlotRelationsByContentSlot(ContentSlotModel contentSlot, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {ContentSlotForPage} WHERE {contentSlot} =?contentSlot AND {catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForPage} WHERE {contentSlot} =?contentSlot AND {catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("contentSlot", contentSlot);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotForPageModel> result = search(fQuery);
        return result.getResult();
    }


    public Collection<CMSRelationModel> findAllContentSlotRelationsByContentSlot(ContentSlotModel contentSlot)
    {
        Collection<CMSRelationModel> ret = new HashSet<>();
        String query = "SELECT {pk} FROM {ContentSlotForPage} WHERE {contentSlot} =?cs";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForPage} WHERE {contentSlot} =?cs");
        fQuery.addQueryParameter("cs", contentSlot);
        SearchResult<CMSRelationModel> result = search(fQuery);
        if(result.getCount() > 0)
        {
            ret.addAll(result.getResult());
        }
        String query2 = "SELECT {pk} FROM {ContentSlotForTemplate} WHERE {contentSlot} =?cs";
        FlexibleSearchQuery fQuery2 = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForTemplate} WHERE {contentSlot} =?cs");
        fQuery2.addQueryParameter("cs", contentSlot);
        SearchResult<CMSRelationModel> result2 = search(fQuery2);
        if(result2.getCount() > 0)
        {
            ret.addAll(result2.getResult());
        }
        return ret;
    }


    public Collection<CMSRelationModel> findOnlyContentSlotRelationsByContentSlot(ContentSlotModel contentSlot)
    {
        Collection<CMSRelationModel> ret = new HashSet<>();
        String query = "SELECT {pk} FROM {ContentSlotForPage} WHERE {contentSlot} =?cs";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForPage} WHERE {contentSlot} =?cs");
        fQuery.addQueryParameter("cs", contentSlot);
        SearchResult<CMSRelationModel> result = search(fQuery);
        if(result.getCount() > 0)
        {
            ret.addAll(result.getResult());
        }
        return ret;
    }


    public List<ContentSlotModel> findContentSlotsByIdAndCatalogVersions(String id, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {pk} FROM {ContentSlot} WHERE {uid} =?uid AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("uid", id);
        SearchResult<ContentSlotModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<ContentSlotModel> findContentSlotsByIdAndCatalogVersions(List<String> slotIds, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {pk} ");
        queryBuilder.append("   FROM { ContentSlot} WHERE ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{uid} in (?uids)", "uids", "OR", slotIds, queryParameters));
        queryBuilder.append(" AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        SearchResult<ContentSlotModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<ContentSlotModel> findContentSlotsById(String id)
    {
        String query = "SELECT {pk} FROM {ContentSlot} WHERE {uid} =?uid";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlot} WHERE {uid} =?uid");
        Map<String, Object> params = new HashMap<>();
        params.put("uid", id);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForPageModel> findContentSlotRelationsByPageAndContentSlot(AbstractPageModel page, ContentSlotModel contentSlot, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {ContentSlotForPage} WHERE {page} =?page  AND {contentSlot} =?contentSlot AND  {catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForPage} WHERE {page} =?page  AND {contentSlot} =?contentSlot AND  {catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("contentSlot", contentSlot);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotForPageModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForPageModel> findContentSlotRelationsByPageAndContentSlot(AbstractPageModel page, ContentSlotModel contentSlot, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {pk} FROM {ContentSlotForPage} WHERE {page} =?page AND {contentSlot} =?contentSlot AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("page", page);
        queryParameters.put("contentSlot", contentSlot);
        SearchResult<ContentSlotForPageModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<ContentSlotForPageModel> findContentSlotRelationsByPageAndPosition(AbstractPageModel page, String position, CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {ContentSlotForPage} WHERE {page} =?page AND {position} =?position AND {catalogVersion} =?catalogVersion";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForPage} WHERE {page} =?page AND {position} =?position AND {catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("position", position);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotForPageModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForPageModel> findContentSlotRelationsByPageAndPosition(AbstractPageModel page, String position, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {pk}  FROM {ContentSlotForPage}  WHERE {page} =?page AND {position} =?position AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{catalogVersion} in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("page", page);
        queryParameters.put("position", position);
        SearchResult<ContentSlotForPageModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public Collection<ContentSlotModel> findContentSlotsForCatalogVersion(CatalogVersionModel catalogVersion)
    {
        String query = "SELECT {pk} FROM {ContentSlot} WHERE {catalogVersion} =?catalogVersion";
        Map<String, Object> params = new HashMap<>();
        params.put("catalogVersion", catalogVersion);
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlot} WHERE {catalogVersion} =?catalogVersion");
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotModel> result = search(fQuery);
        return result.getResult();
    }


    public List<ContentSlotForTemplateModel> findAllContentSlotForTemplateByContentSlot(ContentSlotModel contentSlot, CatalogVersionModel catalogVersion)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlotForTemplate} WHERE {contentSlot} =?contentSlot AND {catalogVersion} =?catalogVersion");
        Map<String, Object> params = new HashMap<>();
        params.put("contentSlot", contentSlot);
        params.put("catalogVersion", catalogVersion);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotForTemplateModel> result = search(fQuery);
        return result.getResult();
    }


    @Deprecated(since = "2105", forRemoval = true)
    public List<ContentSlotModel> findAllMultiCountryContentSlotsByOriginalSlots(List<ContentSlotModel> contentSlots, List<CatalogVersionModel> catalogVersions)
    {
        String query = "SELECT {pk} FROM {ContentSlot} WHERE {active} =?active AND {originalSlot} IN (?slots) AND {catalogVersion} IN (?catalogVersions)";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {ContentSlot} WHERE {active} =?active AND {originalSlot} IN (?slots) AND {catalogVersion} IN (?catalogVersions)");
        Map<String, Object> params = new HashMap<>();
        params.put("slots", contentSlots);
        params.put("catalogVersions", catalogVersions);
        params.put("active", Boolean.TRUE);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotModel> result = search(fQuery);
        return result.getResult();
    }


    @Deprecated(since = "2202", forRemoval = true)
    public List<ContentSlotModel> findAllMultiCountryContentSlotsByOriginalSlots(List<ContentSlotModel> contentSlots, List<CatalogVersionModel> catalogVersions, AbstractPageModel page)
    {
        PageTemplateModel masterTemplate = page.getMasterTemplate();
        String query = "SELECT {pk} FROM {ContentSlot as cs  LEFT JOIN ContentSlotForPage as csp ON {cs.pk} = {csp.contentSlot} LEFT JOIN ContentSlotForTemplate as cst ON {cs.pk} = {cst.contentSlot}}WHERE {cs.active} =?active AND {cs.originalSlot} IN (?slots) AND {cs.catalogVersion} IN (?catalogVersions) AND (({cst. pageTemplate} IS NULL  AND {csp.page} = ?page) OR ({csp.page} IS NULL  AND {cst. pageTemplate} = ?template) OR ({cst. pageTemplate} = ?template  AND {csp.page} = ?page) )";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
                        "SELECT {pk} FROM {ContentSlot as cs  LEFT JOIN ContentSlotForPage as csp ON {cs.pk} = {csp.contentSlot} LEFT JOIN ContentSlotForTemplate as cst ON {cs.pk} = {cst.contentSlot}}WHERE {cs.active} =?active AND {cs.originalSlot} IN (?slots) AND {cs.catalogVersion} IN (?catalogVersions) AND (({cst. pageTemplate} IS NULL  AND {csp.page} = ?page) OR ({csp.page} IS NULL  AND {cst. pageTemplate} = ?template) OR ({cst. pageTemplate} = ?template  AND {csp.page} = ?page) )");
        Map<String, Object> params = new HashMap<>();
        params.put("slots", contentSlots);
        params.put("catalogVersions", catalogVersions);
        params.put("active", Boolean.TRUE);
        params.put("page", page);
        params.put("template", masterTemplate);
        fQuery.addQueryParameters(params);
        SearchResult<ContentSlotModel> result = search(fQuery);
        return result.getResult();
    }


    public List<CMSRelationModel> getAllDeletedRelationsForPage(CatalogVersionModel targetCatalogVersion, AbstractPageModel sourcePage)
    {
        PageTemplateModel sourcePageMasterTemplate = sourcePage.getMasterTemplate();
        CatalogVersionModel sourceCatalogVersion = sourcePage.getCatalogVersion();
        String contentSlotForPagesQuery = "SELECT {pk} FROM {ContentSlotForPage AS TARGET LEFT JOIN ContentSlotForPage AS SOURCE ON {TARGET.uid} = {SOURCE.uid} AND {SOURCE.catalogVersion} = ?sourceCatalogVersion LEFT JOIN AbstractPage AS PAGE ON {TARGET.page} = {PAGE.pk} AND {PAGE.uid} = ?sourcePageUid } WHERE {TARGET.catalogVersion} = ?targetCatalogVersion AND {TARGET.pk} IS NOT NULL AND {SOURCE.pk} IS NULL ";
        String contentSlotForTemplateQuery = "SELECT {pk} FROM {ContentSlotForTemplate AS TARGET LEFT JOIN ContentSlotForTemplate AS SOURCE ON {TARGET.uid} = {SOURCE.uid} AND {SOURCE.catalogVersion} = ?sourceCatalogVersion LEFT JOIN PageTemplate AS TEMPLATE ON {TARGET.pageTemplate} = {TEMPLATE.pk} AND {TEMPLATE.uid} = ?sourceTemplateUid } WHERE {TARGET.catalogVersion} = ?targetCatalogVersion AND {TARGET.pk} IS NOT NULL AND {SOURCE.pk} IS NULL ";
        String query = "SELECT tbl.pk FROM ({{SELECT {pk} FROM {ContentSlotForPage AS TARGET LEFT JOIN ContentSlotForPage AS SOURCE ON {TARGET.uid} = {SOURCE.uid} AND {SOURCE.catalogVersion} = ?sourceCatalogVersion LEFT JOIN AbstractPage AS PAGE ON {TARGET.page} = {PAGE.pk} AND {PAGE.uid} = ?sourcePageUid } WHERE {TARGET.catalogVersion} = ?targetCatalogVersion AND {TARGET.pk} IS NOT NULL AND {SOURCE.pk} IS NULL }} UNION ALL {{SELECT {pk} FROM {ContentSlotForTemplate AS TARGET LEFT JOIN ContentSlotForTemplate AS SOURCE ON {TARGET.uid} = {SOURCE.uid} AND {SOURCE.catalogVersion} = ?sourceCatalogVersion LEFT JOIN PageTemplate AS TEMPLATE ON {TARGET.pageTemplate} = {TEMPLATE.pk} AND {TEMPLATE.uid} = ?sourceTemplateUid } WHERE {TARGET.catalogVersion} = ?targetCatalogVersion AND {TARGET.pk} IS NOT NULL AND {SOURCE.pk} IS NULL }}) tbl";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
                        "SELECT tbl.pk FROM ({{SELECT {pk} FROM {ContentSlotForPage AS TARGET LEFT JOIN ContentSlotForPage AS SOURCE ON {TARGET.uid} = {SOURCE.uid} AND {SOURCE.catalogVersion} = ?sourceCatalogVersion LEFT JOIN AbstractPage AS PAGE ON {TARGET.page} = {PAGE.pk} AND {PAGE.uid} = ?sourcePageUid } WHERE {TARGET.catalogVersion} = ?targetCatalogVersion AND {TARGET.pk} IS NOT NULL AND {SOURCE.pk} IS NULL }} UNION ALL {{SELECT {pk} FROM {ContentSlotForTemplate AS TARGET LEFT JOIN ContentSlotForTemplate AS SOURCE ON {TARGET.uid} = {SOURCE.uid} AND {SOURCE.catalogVersion} = ?sourceCatalogVersion LEFT JOIN PageTemplate AS TEMPLATE ON {TARGET.pageTemplate} = {TEMPLATE.pk} AND {TEMPLATE.uid} = ?sourceTemplateUid } WHERE {TARGET.catalogVersion} = ?targetCatalogVersion AND {TARGET.pk} IS NOT NULL AND {SOURCE.pk} IS NULL }}) tbl");
        Map<String, Object> params = new HashMap<>();
        params.put("targetCatalogVersion", targetCatalogVersion);
        params.put("sourceCatalogVersion", sourceCatalogVersion);
        params.put("sourceTemplateUid", sourcePageMasterTemplate.getUid());
        params.put("sourcePageUid", sourcePage.getUid());
        fQuery.addQueryParameters(params);
        SearchResult<CMSRelationModel> result = search(fQuery);
        return result.getResult();
    }
}
