package com.hybris.backoffice.solrsearch.indexer.cron;

import com.hybris.backoffice.solrsearch.enums.SolrItemModificationType;
import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated(since = "6.6", forRemoval = true)
public class BackofficeSolrIndexerUpdateJob extends AbstractBackofficeSolrIndexerJob
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeSolrIndexerUpdateJob.class);


    protected Collection<SolrModifiedItemModel> findModifiedItems()
    {
        Collection<SolrModifiedItemModel> byModificationType = this.solrModifiedItemDAO.findByModificationType(SolrItemModificationType.UPDATE);
        return (Collection<SolrModifiedItemModel>)byModificationType.stream().filter(this::isItemExisting).collect(Collectors.toList());
    }


    protected boolean isItemExisting(SolrModifiedItemModel modifiedItem)
    {
        try
        {
            Object model = this.modelService.get(PK.fromLong(modifiedItem.getModifiedPk().longValue()));
            if(model != null)
            {
                return true;
            }
            this.modelService.remove(modifiedItem);
            return false;
        }
        catch(ModelLoadingException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getLocalizedMessage(), (Throwable)e);
            }
            this.modelService.remove(modifiedItem);
            return false;
        }
    }


    protected void synchronizeIndexForType(FacetSearchConfig facetSearchConfig, IndexedType type, Collection<PK> pks) throws IndexerException
    {
        this.indexerService.updateTypeIndex(facetSearchConfig, type, new ArrayList<>(pks));
    }
}
