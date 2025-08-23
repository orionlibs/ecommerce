package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomNumberValueResolver<T extends ItemModel> extends AbstractValueResolver<T, Object, Object>
{
    public static final String ID = "randomNumberValueResolver";
    public static final String VALUE_COUNT_PARAM = "valueCount";
    public static final int VALUE_COUNT_PARAM_DEFAULT_VALUE = 1;
    public static final String MIN_VALUE_PARAM = "minValue";
    public static final int MIN_VALUE_PARAM_DEFAULT_VALUE = 0;
    public static final String MAX_VALUE_PARAM = "maxValue";
    public static final int MAX_VALUE_PARAM_DEFAULT_VALUE = 100;


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, T model, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
    {
        int valueCount = resolveValueCount(indexedProperty);
        int minValue = resolveMinValue(indexedProperty);
        int maxValue = resolveMaxValue(indexedProperty);
        if(valueCount == 1)
        {
            Object value = Integer.valueOf(ThreadLocalRandom.current().nextInt(minValue, maxValue + 1));
            document.addField(indexedProperty, value);
        }
        else if(valueCount > 1)
        {
            Object value = ThreadLocalRandom.current().ints(valueCount, minValue, maxValue + 1).boxed().collect(Collectors.toList());
            document.addField(indexedProperty, value);
        }
    }


    protected int resolveValueCount(IndexedProperty indexedProperty)
    {
        return ValueProviderParameterUtils.getInt(indexedProperty, "valueCount", 1);
    }


    protected int resolveMinValue(IndexedProperty indexedProperty)
    {
        return ValueProviderParameterUtils.getInt(indexedProperty, "minValue", 0);
    }


    protected int resolveMaxValue(IndexedProperty indexedProperty)
    {
        return ValueProviderParameterUtils.getInt(indexedProperty, "maxValue", 100);
    }
}
