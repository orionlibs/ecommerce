package com.hybris.backoffice.searchservices.aspects;

import com.hybris.backoffice.search.aspects.AbstractObjectFacadeSearchIndexingAspect;
import com.hybris.backoffice.searchservices.events.SearchservicesIndexSynchronizationStrategy;
import de.hybris.platform.core.PK;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectFacadeSearchservicesIndexingAspect extends AbstractObjectFacadeSearchIndexingAspect
{
    private static final Logger LOG = LoggerFactory.getLogger(ObjectFacadeSearchservicesIndexingAspect.class);
    public static final String CONFIG_BACKOFFICE_SEARCH_BACKGROUND_SEARCHSERVICES_INDEXING_ENABLED = "backoffice.search.background.searchservices.indexing.enabled";
    private SearchservicesIndexSynchronizationStrategy searchservicesIndexSynchronizationStrategy;


    protected boolean isBackgroundIndexingEnabled()
    {
        return getConfigurationService().getConfiguration()
                        .getBoolean("backoffice.search.background.searchservices.indexing.enabled", false);
    }


    protected void removeIndexByPk(String typecode, List<PK> pkList)
    {
        getSearchservicesIndexSynchronizationStrategy().removeItems(typecode, pkList);
    }


    protected void updateIndexByPk(String typecode, List<PK> pkList)
    {
        getSearchservicesIndexSynchronizationStrategy().updateItems(typecode, pkList);
    }


    protected void logDebug(Map.Entry<String, List<PK>> entry)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Searchservices index items removed for typecode: {}, pks: {}", entry.getKey(), ((List)entry
                            .getValue()).stream().map(Objects::toString).collect(Collectors.joining(",")));
        }
    }


    private SearchservicesIndexSynchronizationStrategy getSearchservicesIndexSynchronizationStrategy()
    {
        return this.searchservicesIndexSynchronizationStrategy;
    }


    public void setSearchservicesIndexSynchronizationStrategy(SearchservicesIndexSynchronizationStrategy searchservicesIndexSynchronizationStrategy)
    {
        this.searchservicesIndexSynchronizationStrategy = searchservicesIndexSynchronizationStrategy;
    }
}
