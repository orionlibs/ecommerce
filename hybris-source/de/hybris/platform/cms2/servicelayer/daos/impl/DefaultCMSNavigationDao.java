package de.hybris.platform.cms2.servicelayer.daos.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSNavigationDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.FlexibleSearchUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;

public class DefaultCMSNavigationDao extends AbstractItemDao implements CMSNavigationDao
{
    protected static final String CATALOG_VERSIONS_QUERY_PARAM = "catalogVersions";
    protected static final String CATALOG_VERSION_QUERY_PARAM = "catalogVersion";
    protected static final String PAGE_QUERY_PARAM = "page";
    protected static final String ENTRY_QUERY_PARAM = "entry";


    public List<CMSNavigationNodeModel> findNavigationNodesByContentPage(ContentPageModel page, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {n:target} FROM {" + GeneratedCms2Constants.Relations.CMSCONTENTPAGESFORNAVNODES + " AS n LEFT JOIN CMSNavigationNode AS r  ON {n:target} = {r:pk}} WHERE {n:source} = ?page AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{r:catalogVersion}  in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryBuilder.append("AND {n:target} NOT IN ( ");
        queryBuilder.append("{{ SELECT {root:pk} ");
        queryBuilder.append(" FROM {CMSNavigationNode AS root }");
        queryBuilder.append(" WHERE {root:parent} IS NULL AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{root:catalogVersion}  in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryBuilder.append(" }} )");
        queryParameters.put("page", page);
        SearchResult<CMSNavigationNodeModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<CMSNavigationNodeModel> findNavigationNodesById(String id, Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {n:pk} FROM {CMSNavigationNode AS n } WHERE {n:uid} = ?uid  AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{n:catalogVersion}  in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryParameters.put("uid", id);
        SearchResult<CMSNavigationNodeModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public List<CMSNavigationNodeModel> findRootNavigationNodes(Collection<CatalogVersionModel> catalogVersions)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder.append("SELECT {n:pk} FROM {CMSNavigationNode AS n } WHERE ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{n:catalogVersion}  in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryBuilder.append("AND {n:parent} IN ( ");
        queryBuilder.append("{{ SELECT {root:pk} ");
        queryBuilder.append(" FROM {CMSNavigationNode AS root }");
        queryBuilder.append(" WHERE {root:parent} IS NULL AND ");
        queryBuilder.append(
                        FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{root:catalogVersion}  in (?catalogVersions)", "catalogVersions", "OR", catalogVersions, queryParameters));
        queryBuilder.append(" }} )");
        queryBuilder.append(" ORDER BY {n:parentPOS} ");
        SearchResult<CMSNavigationNodeModel> searchResults = search(queryBuilder.toString(), queryParameters);
        return searchResults.getResult();
    }


    public CMSNavigationNodeModel findSuperRootNavigationNode(CatalogVersionModel catalogVersion)
    {
        CMSNavigationNodeModel ret = null;
        if(catalogVersion == null)
        {
            return ret;
        }
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("catalogVersion", catalogVersion);
        queryBuilder.append("SELECT {n:pk} FROM {CMSNavigationNode AS n } WHERE {n:parent} IS NULL  AND {n:catalogVersion} = (?catalogVersion)");
        SearchResult<CMSNavigationNodeModel> searchResult = search(queryBuilder.toString(), queryParameters);
        if(CollectionUtils.isNotEmpty(searchResult.getResult()))
        {
            ret = searchResult.getResult().stream().filter(cmsNavigationNodeModel -> "root".equalsIgnoreCase(cmsNavigationNodeModel.getUid())).findFirst().orElse(null);
        }
        return ret;
    }


    public List<CMSNavigationEntryModel> findNavigationEntriesByPage(AbstractPageModel page)
    {
        Preconditions.checkArgument(Objects.nonNull(page), "AbstractPageModel page must be supplied.");
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("page", page);
        queryBuilder.append("SELECT {e:pk} FROM {CMSNavigationEntry AS e JOIN CMSNavigationNode AS nn ON {nn:pk} = {e:navigationNode}} WHERE {e:item} IN ({{ SELECT {PK} FROM {AbstractPage} WHERE {PK} = ?page }}) OR {e:item} IN ({{ SELECT {PK} FROM {CMSLinkComponent} WHERE {contentPage} = ?page}})");
        SearchResult<CMSNavigationEntryModel> searchResult = search(queryBuilder.toString(), queryParameters);
        return searchResult.getResult();
    }


    public CMSNavigationEntryModel findNavigationEntryByUid(String uid, CatalogVersionModel catalogVersion)
    {
        Preconditions.checkArgument(Objects.nonNull(uid), "CMSNavigationEntryModel uid must be supplied.");
        Preconditions.checkArgument(Objects.nonNull(catalogVersion), "CatalogVersionModel catalogVersion must be supplied.");
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("entry", uid);
        queryParameters.put("catalogVersion", catalogVersion);
        queryBuilder.append("SELECT {e:pk} FROM {CMSNavigationEntry AS e }WHERE {e:uid} = ?entry AND {e:catalogVersion} = ?catalogVersion");
        SearchResult<CMSNavigationEntryModel> searchResult = search(queryBuilder.toString(), queryParameters);
        return searchResult.getResult().stream().findFirst().orElse(null);
    }
}
