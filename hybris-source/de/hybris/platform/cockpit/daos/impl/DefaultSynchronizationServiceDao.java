package de.hybris.platform.cockpit.daos.impl;

import de.hybris.platform.cockpit.daos.SynchronizationServiceDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSynchronizationServiceDao implements SynchronizationServiceDao
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSynchronizationServiceDao.class);
    private static final String SOURCES_QUERY = "SELECT {ist:sourceItem}, {ist:sourceVersion} FROM {ItemSyncTimestamp AS ist} WHERE {ist:targetItem} = ?target AND {ist:sourceVersion} IS NOT NULL";
    private static final String TARGETS_QUERY = "SELECT {ist:targetItem}, {ist:targetVersion} FROM {ItemSyncTimestamp AS ist} WHERE {ist:sourceItem} = ?source AND {ist:targetVersion} IS NOT NULL";
    private static final String SOURCES_TARGETS_QUERY = "SELECT * FROM ({{SELECT {ist:sourceItem}, {ist:sourceVersion} FROM {ItemSyncTimestamp AS ist} WHERE {ist:targetItem} = ?target AND {ist:sourceVersion} IS NOT NULL}} UNION ALL {{SELECT {ist:targetItem}, {ist:targetVersion} FROM {ItemSyncTimestamp AS ist} WHERE {ist:sourceItem} = ?source AND {ist:targetVersion} IS NOT NULL}}) AS SourcesTargets";
    private FlexibleSearchService flexibleSearchService;


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public SearchResult<Object> getSyncSources(ItemModel model)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("target", model);
        return search("SELECT {ist:sourceItem}, {ist:sourceVersion} FROM {ItemSyncTimestamp AS ist} WHERE {ist:targetItem} = ?target AND {ist:sourceVersion} IS NOT NULL", params);
    }


    public SearchResult<Object> getSyncTargets(ItemModel model)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("source", model);
        return search("SELECT {ist:targetItem}, {ist:targetVersion} FROM {ItemSyncTimestamp AS ist} WHERE {ist:sourceItem} = ?source AND {ist:targetVersion} IS NOT NULL", params);
    }


    public SearchResult<Object> getSyncSourcesAndTargets(ItemModel model)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("source", model);
        params.put("target", model);
        return search("SELECT * FROM ({{SELECT {ist:sourceItem}, {ist:sourceVersion} FROM {ItemSyncTimestamp AS ist} WHERE {ist:targetItem} = ?target AND {ist:sourceVersion} IS NOT NULL}} UNION ALL {{SELECT {ist:targetItem}, {ist:targetVersion} FROM {ItemSyncTimestamp AS ist} WHERE {ist:sourceItem} = ?source AND {ist:targetVersion} IS NOT NULL}}) AS SourcesTargets",
                        params);
    }


    private SearchResult<Object> search(String query, Map<String, Object> params)
    {
        SearchResult<Object> result = this.flexibleSearchService.search(query, params);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Search for " + params.entrySet().iterator().next());
            LOG.debug("Search query: " + query);
            LOG.debug("" + result.getCount() + " items found");
        }
        return result;
    }
}
