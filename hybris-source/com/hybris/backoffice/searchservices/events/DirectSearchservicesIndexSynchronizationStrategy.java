package com.hybris.backoffice.searchservices.events;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import com.hybris.backoffice.searchservices.services.impl.BackofficeSearchservicesFacetSearchConfigService;
import de.hybris.platform.core.PK;
import de.hybris.platform.searchservices.enums.SnDocumentOperationType;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerService;
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerItemSourceOperation;
import de.hybris.platform.searchservices.indexer.service.impl.PksSnIndexerItemSource;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectSearchservicesIndexSynchronizationStrategy implements SearchservicesIndexSynchronizationStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DirectSearchservicesIndexSynchronizationStrategy.class);
    protected SnIndexerService snIndexerService;
    protected ModelService modelService;
    protected TypeService typeService;
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;


    public void updateItem(String typecode, long pk)
    {
        updateItems(typecode, Collections.singletonList(PK.fromLong(pk)));
    }


    public void updateItems(String typecode, List<PK> pkList)
    {
        SnIndexTypeModel indexType = (SnIndexTypeModel)this.backofficeFacetSearchConfigService.getIndexedTypeModel(typecode);
        if(indexType != null && !pkList.isEmpty() && isIndexInitialized(typecode))
        {
            performIndexUpdate(indexType, pkList);
        }
    }


    public void removeItem(String typecode, long pk)
    {
        removeItems(typecode, Collections.singletonList(PK.fromLong(pk)));
    }


    public void removeItems(String typecode, List<PK> pkList)
    {
        SnIndexTypeModel indexType = (SnIndexTypeModel)this.backofficeFacetSearchConfigService.getIndexedTypeModel(typecode);
        if(indexType != null && !pkList.isEmpty() && isIndexInitialized(typecode))
        {
            performIndexDelete(indexType, pkList);
        }
    }


    protected void performIndexDelete(SnIndexTypeModel indexType, List<PK> pks)
    {
        try
        {
            List<SnIndexerItemSourceOperation> indexerItemSourceOperations = new ArrayList<>();
            PksSnIndexerItemSource pksSnIndexerItemSource = new PksSnIndexerItemSource(pks);
            DefaultSnIndexerItemSourceOperation indexerItemSourceOperation = new DefaultSnIndexerItemSourceOperation(SnDocumentOperationType.DELETE, (SnIndexerItemSource)pksSnIndexerItemSource);
            indexerItemSourceOperations.add(indexerItemSourceOperation);
            SnIndexerRequest indexerRequest = this.snIndexerService.createIncrementalIndexerRequest(indexType.getId(), indexerItemSourceOperations);
            this.snIndexerService.index(indexerRequest);
        }
        catch(SnIndexerException e)
        {
            LOG.error(MessageFormat.format("Error running delete index for index type ''{0}''", new Object[] {indexType.getId()}), (Throwable)e);
        }
    }


    protected void performIndexUpdate(SnIndexTypeModel indexType, List<PK> pks)
    {
        try
        {
            List<SnIndexerItemSourceOperation> indexerItemSourceOperations = new ArrayList<>();
            PksSnIndexerItemSource pksSnIndexerItemSource = new PksSnIndexerItemSource(pks);
            DefaultSnIndexerItemSourceOperation indexerItemSourceOperation = new DefaultSnIndexerItemSourceOperation(SnDocumentOperationType.CREATE_UPDATE, (SnIndexerItemSource)pksSnIndexerItemSource);
            indexerItemSourceOperations.add(indexerItemSourceOperation);
            SnIndexerRequest indexerRequest = this.snIndexerService.createIncrementalIndexerRequest(indexType.getId(), indexerItemSourceOperations);
            this.snIndexerService.index(indexerRequest);
        }
        catch(SnIndexerException e)
        {
            LOG.error(MessageFormat.format("Error running update index for index type ''{0}''", new Object[] {indexType.getId()}), (Throwable)e);
        }
    }


    private boolean isIndexInitialized(String typecode)
    {
        BackofficeSearchservicesFacetSearchConfigService backofficeSearchservicesFacetSearchConfigService = (BackofficeSearchservicesFacetSearchConfigService)this.backofficeFacetSearchConfigService;
        BackofficeIndexedTypeToSearchservicesIndexConfigModel smartSearchConfig = backofficeSearchservicesFacetSearchConfigService.findSearchConfigForTypeCode(typecode);
        return smartSearchConfig.isActive();
    }


    public void setSnIndexerService(SnIndexerService snIndexerService)
    {
        this.snIndexerService = snIndexerService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
