package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ItemModelPKValueResolver extends AbstractValueResolver<ItemModel, Object, Object>
{
    private ModelService modelService;
    public static final Logger LOG = LoggerFactory.getLogger(ItemModelPKValueResolver.class);


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ItemModel model, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
    {
        Optional<ItemModel> targetModel = getTargetModel(model, indexedProperty);
        if(targetModel.isPresent())
        {
            Long pkLongValue = ((ItemModel)targetModel.get()).getPk().getLong();
            document.addField(String.format("%s_%s", new Object[] {indexedProperty.getName(), indexedProperty.getType()}), pkLongValue);
        }
    }


    protected Optional<ItemModel> getTargetModel(ItemModel model, IndexedProperty indexedProperty)
    {
        String providerParameter = indexedProperty.getValueProviderParameter();
        if(StringUtils.isNotEmpty(providerParameter))
        {
            return Optional.ofNullable((ItemModel)getModelService().getAttributeValue(model, providerParameter));
        }
        return getTargetModel(model);
    }


    protected Optional<ItemModel> getTargetModel(ItemModel sourceModel)
    {
        return Optional.ofNullable(sourceModel);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
