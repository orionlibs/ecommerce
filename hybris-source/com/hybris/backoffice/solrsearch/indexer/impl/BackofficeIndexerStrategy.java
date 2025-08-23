package com.hybris.backoffice.solrsearch.indexer.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.impl.DefaultIndexerStrategy;
import de.hybris.platform.solrfacetsearch.solr.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackofficeIndexerStrategy extends DefaultIndexerStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeIndexerStrategy.class);


    protected void doExecute(Index resolvedIndex, long indexOperationId, boolean isExternalIndexOperation) throws IndexerException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Executing indexer worker as an admin user.");
        }
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, resolvedIndex, indexOperationId, isExternalIndexOperation), (UserModel)
                        getUserService().getAdminUser());
    }


    protected void doExecuteAsAdmin(Index resolvedIndex, long indexOperationId, boolean isExternalIndexOperation) throws IndexerException
    {
        super.doExecute(resolvedIndex, indexOperationId, isExternalIndexOperation);
    }
}
