package de.hybris.platform.cms2.servicelayer.daos.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSVersionDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class DefaultCMSVersionDao extends AbstractItemDao implements CMSVersionDao
{
    public Optional<CMSVersionModel> findByUid(String uid)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(uid), "uid must neither be null nor empty");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {version.pk} ")
                        .append("FROM {CMSVersion AS version} ")
                        .append("WHERE {uid} = ?uid");
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("uid", uid);
        SearchResult<CMSVersionModel> result = search(queryBuilder.toString(), queryParameters);
        if(result.getCount() > 0)
        {
            return Optional.of(result.getResult().iterator().next());
        }
        return Optional.empty();
    }


    public Optional<CMSVersionModel> findByItemUidAndLabel(String itemUid, String label, CatalogVersionModel itemCatalogVersion)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(itemUid), "itemUid must neither be null nor empty");
        Preconditions.checkArgument(StringUtils.isNotBlank(label), "label must neither be null nor empty");
        Preconditions.checkArgument(Objects.nonNull(itemCatalogVersion), "itemCatalogVersion must not be null");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {version.pk} ")
                        .append("FROM {CMSVersion AS version} ")
                        .append("WHERE {itemUid} = ?itemUid AND ")
                        .append("{label} = ?label AND ")
                        .append("{itemCatalogVersion} = ?itemCatalogVersion");
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("itemUid", itemUid);
        queryParameters.put("label", label);
        queryParameters.put("itemCatalogVersion", itemCatalogVersion);
        SearchResult<CMSVersionModel> result = search(queryBuilder.toString(), queryParameters);
        if(result.getCount() > 0)
        {
            return Optional.of(result.getResult().iterator().next());
        }
        return Optional.empty();
    }


    public List<CMSVersionModel> findAllByItemUidAndItemCatalogVersion(String itemUid, CatalogVersionModel itemCatalogVersion)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(itemUid), "itemUid must neither be null nor empty");
        Preconditions.checkArgument(Objects.nonNull(itemCatalogVersion), "itemCatalogVersion must neither be null nor empty");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {version.pk} ")
                        .append("FROM {CMSVersion AS version} ")
                        .append("WHERE {itemUid} = ?itemUid AND ")
                        .append("{itemCatalogVersion} = ?itemCatalogVersion");
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("itemUid", itemUid);
        queryParameters.put("itemCatalogVersion", itemCatalogVersion);
        SearchResult<CMSVersionModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }


    public Optional<AbstractPageModel> findPageVersionedByTransactionId(String transactionId)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(transactionId), "transactionId must neither be null nor empty");
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryBuilder
                        .append("SELECT {page.pk} ")
                        .append("FROM ")
                        .append("{ ")
                        .append("AbstractPage AS page JOIN CMSVersion AS version ")
                        .append("ON {page.uid} = {version.itemUid} AND ")
                        .append("{page.catalogVersion} = {version.itemCatalogVersion} ")
                        .append("JOIN CMSPageType AS type ")
                        .append("ON {version.itemTypeCode} = {type.code} ")
                        .append("} ")
                        .append("WHERE {version.transactionId}=?transactionId");
        queryParameters.put("transactionId", transactionId);
        SearchResult<AbstractPageModel> result = search(queryBuilder.toString(), queryParameters);
        if(result.getCount() > 0)
        {
            return Optional.of(result.getResult().iterator().next());
        }
        return Optional.empty();
    }
}
