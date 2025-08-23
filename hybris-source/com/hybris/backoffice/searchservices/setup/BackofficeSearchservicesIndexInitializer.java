package com.hybris.backoffice.searchservices.setup;

import com.hybris.backoffice.search.setup.AbstractBackofficeSearchIndexInitializer;
import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerResponse;
import de.hybris.platform.searchservices.indexer.service.SnIndexerService;
import de.hybris.platform.searchservices.model.AbstractSnIndexerCronJobModel;
import de.hybris.platform.searchservices.model.FullSnIndexerCronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackofficeSearchservicesIndexInitializer extends AbstractBackofficeSearchIndexInitializer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BackofficeSearchservicesIndexInitializer.class);
    private SnIndexerService snIndexerService;
    private SnIndexerItemSourceFactory snIndexerItemSourceFactory;
    private ModelService modelService;


    protected void initializeIndexesIfNecessary()
    {
        if(shouldInitializeIndexes())
        {
            initializeIndex();
        }
        else
        {
            LOGGER.info("Backoffice Searchservices indices initialization disabled by property");
        }
    }


    protected boolean shouldInitializeIndexes()
    {
        return Config.getBoolean("backoffice.search.services.index.autoinit", true);
    }


    public void setSnIndexerService(SnIndexerService snIndexerService)
    {
        this.snIndexerService = snIndexerService;
    }


    public void setSnIndexerItemSourceFactory(SnIndexerItemSourceFactory snIndexerItemSourceFactory)
    {
        this.snIndexerItemSourceFactory = snIndexerItemSourceFactory;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    private void initializeIndex()
    {
        this.backofficeFacetSearchConfigService.getAllMappedFacetSearchConfigs().stream()
                        .filter(searchConfigMapping -> !isIndexInitialized(searchConfigMapping)).forEach(this::fullIndex);
    }


    private boolean isIndexInitialized(Object searchConfigMapping)
    {
        BackofficeIndexedTypeToSearchservicesIndexConfigModel smartSearchConfigMapping = (BackofficeIndexedTypeToSearchservicesIndexConfigModel)searchConfigMapping;
        return smartSearchConfigMapping.isActive();
    }


    private void fullIndex(Object searchConfigMapping)
    {
        LOGGER.info("Performing FULL INDEX operation for '{}' configuration", ((BackofficeIndexedTypeToSearchservicesIndexConfigModel)searchConfigMapping)
                        .getSnIndexConfiguration().getName());
        BackofficeIndexedTypeToSearchservicesIndexConfigModel smartSearchConfigMapping = (BackofficeIndexedTypeToSearchservicesIndexConfigModel)searchConfigMapping;
        String snIndexType = smartSearchConfigMapping.getSnIndexType().getId();
        List<AbstractSnIndexerCronJobModel> snIndexerCronJobModels = smartSearchConfigMapping.getSnIndexType().getIndexerCronJobs();
        AtomicBoolean isSuccessful = new AtomicBoolean(true);
        Collection<Object> beUpdatedObjects = new ArrayList(0);
        snIndexerCronJobModels.forEach(snIndexerCronJobModel -> {
            if(snIndexerCronJobModel instanceof FullSnIndexerCronJobModel)
            {
                try
                {
                    FullSnIndexerCronJobModel fullSnIndexerCronJobModel = (FullSnIndexerCronJobModel)snIndexerCronJobModel;
                    SnIndexerResponse snIndexerResponse = performFullIndex(snIndexType, fullSnIndexerCronJobModel);
                    beUpdatedObjects.add(snIndexerCronJobModel);
                    String debugInfo = String.format("First %d and then %d", new Object[] {snIndexerResponse.getTotalItems(), snIndexerResponse.getProcessedItems()});
                    LOGGER.info(debugInfo);
                }
                catch(SnIndexerException exception)
                {
                    isSuccessful.set(false);
                    LOGGER.error(exception.getMessage());
                    return;
                }
            }
        });
        if(isSuccessful.get() && !beUpdatedObjects.isEmpty())
        {
            ((BackofficeIndexedTypeToSearchservicesIndexConfigModel)searchConfigMapping).setActive(true);
            beUpdatedObjects.add(searchConfigMapping);
            this.modelService.saveAll(beUpdatedObjects);
            LOGGER.info("Successfully perform FULL INDEX operation for '{}' index type", ((BackofficeIndexedTypeToSearchservicesIndexConfigModel)searchConfigMapping)
                            .getSnIndexType().getName());
        }
        else
        {
            LOGGER.info("Fail to perform FULL INDEX operation for '{}' index type", ((BackofficeIndexedTypeToSearchservicesIndexConfigModel)searchConfigMapping)
                            .getSnIndexType().getName());
        }
    }


    private SnIndexerResponse performFullIndex(String snIndexType, FullSnIndexerCronJobModel fullSnIndexerCronJobModel) throws SnIndexerException
    {
        SnIndexerItemSource snIndexerItemSource = this.snIndexerItemSourceFactory.createItemSource(fullSnIndexerCronJobModel.getIndexerItemSource(), Collections.emptyMap());
        SnIndexerRequest snIndexerRequest = this.snIndexerService.createFullIndexerRequest(snIndexType, snIndexerItemSource);
        fullSnIndexerCronJobModel.setStartTime(new Date());
        SnIndexerResponse snIndexerResponse = this.snIndexerService.index(snIndexerRequest);
        fullSnIndexerCronJobModel.setEndTime(new Date());
        fullSnIndexerCronJobModel.setLastSuccessfulStartTime(fullSnIndexerCronJobModel.getEndTime());
        fullSnIndexerCronJobModel.setResult(CronJobResult.SUCCESS);
        return snIndexerResponse;
    }
}
