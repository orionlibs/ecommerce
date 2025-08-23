package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnumValueResolver extends AbstractValueResolver<ItemModel, Object, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(EnumValueResolver.class);
    private ModelService modelService;


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ItemModel model, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext)
    {
        String indexedPropertyName = indexedProperty.getName();
        try
        {
            Object modelProperty = getModelService().getAttributeValue(model, indexedPropertyName);
            if(isHybrisEnum(modelProperty))
            {
                HybrisEnumValue hybrisEnum = (HybrisEnumValue)modelProperty;
                document.addField(indexedProperty, getEnumValue(hybrisEnum));
            }
            else
            {
                LOG.warn("Resolving value for IndexedProperty: {} has failed because it's not a HybrisEnumValue", indexedProperty
                                .getName());
            }
        }
        catch(Exception ex)
        {
            LOG.debug(String.format("Cannot resolve property '%s' for type %s", new Object[] {indexedProperty.getName(), model
                            .toString()}), ex);
        }
    }


    protected boolean isHybrisEnum(Object modelProperty)
    {
        return modelProperty instanceof HybrisEnumValue;
    }


    protected String getEnumValue(HybrisEnumValue hybrisEnumValue)
    {
        return hybrisEnumValue.getCode();
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
