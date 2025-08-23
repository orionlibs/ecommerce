package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.impl.ModelAttributeSnIndexerValueProvider;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public class ClassificationSnIndexerValueProvider extends ModelAttributeSnIndexerValueProvider
{
    protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, ItemModel source, Void data) throws SnIndexerException
    {
        Object fieldValues = super.getFieldValue(indexerContext, fieldWrapper, source, data);
        if(fieldValues instanceof Collection && CollectionUtils.isNotEmpty((Collection)fieldValues))
        {
            return ((Collection)fieldValues).stream().distinct().collect(Collectors.toList());
        }
        return fieldValues;
    }
}
