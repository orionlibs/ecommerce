package com.hybris.backoffice.searchservices.indexer.listeners;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import com.hybris.backoffice.searchservices.services.impl.BackofficeSearchservicesFacetSearchConfigService;
import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerListener;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Optional;

public class BackofficeSnIndexerListener implements SnIndexerListener
{
    private BackofficeFacetSearchConfigService<Object, Object, SnIndexTypeModel, Object> backofficeFacetSearchConfigService;
    private ModelService modelService;


    public void beforeIndex(SnIndexerContext context)
    {
    }


    public void afterIndex(SnIndexerContext context)
    {
        if(context.getIndexerOperationType() != SnIndexerOperationType.FULL)
        {
            return;
        }
        String itemType = context.getIndexType().getItemComposedType();
        String indexTypeId = context.getIndexType().getId();
        BackofficeSearchservicesFacetSearchConfigService backofficeSearchservicesFacetSearchConfigService = (BackofficeSearchservicesFacetSearchConfigService)this.backofficeFacetSearchConfigService;
        Optional<BackofficeIndexedTypeToSearchservicesIndexConfigModel> searchConfigOptional = backofficeSearchservicesFacetSearchConfigService.findSearchConfigForTypeCodeAndIndexTypeId(itemType, indexTypeId);
        if(searchConfigOptional.isPresent())
        {
            BackofficeIndexedTypeToSearchservicesIndexConfigModel searchConfig = searchConfigOptional.get();
            if(!searchConfig.isActive())
            {
                searchConfig.setActive(true);
                this.modelService.save(searchConfig);
            }
        }
    }


    public void afterIndexError(SnIndexerContext context)
    {
    }


    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService<Object, Object, SnIndexTypeModel, Object> backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
