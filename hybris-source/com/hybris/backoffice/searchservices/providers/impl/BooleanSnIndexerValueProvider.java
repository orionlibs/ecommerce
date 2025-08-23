package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.impl.ModelAttributeSnIndexerValueProvider;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;

public class BooleanSnIndexerValueProvider extends ModelAttributeSnIndexerValueProvider
{
    protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, ItemModel source, Void data) throws SnIndexerException
    {
        Object fieldValue = super.getFieldValue(indexerContext, fieldWrapper, source, data);
        if(fieldValue instanceof Collection)
        {
            return Boolean.valueOf(CollectionUtils.isEmpty((Collection)fieldValue));
        }
        return fieldValue;
    }
}
