package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomStringValueResolver<T extends ItemModel> extends AbstractValueResolver<T, Object, Object>
{
    public static final String ID = "randomStringValueResolver";
    public static final String VALUE_COUNT_PARAM = "valueCount";
    public static final int VALUE_COUNT_PARAM_DEFAULT_VALUE = 1;
    public static final String DISTINCT_VALUE_COUNT_PARAM = "distinctValueCount";
    public static final int DISTINCT_VALUE_COUNT_PARAM_DEFAULT_VALUE = 1;
    public static final String PREFIX_PARAM = "prefix";
    public static final String PREFIX_PARAM_DEFAULT_VALUE = "value";


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, T model, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
    {
        int valueCount = resolveValueCount(indexedProperty);
        int distinctValueCount = resolveDistinctValueCount(indexedProperty);
        String prefix = resolvePrefix(indexedProperty);
        if(valueCount == 1)
        {
            Object value = prefix + prefix;
            document.addField(indexedProperty, value);
        }
        else if(valueCount > 1)
        {
            Object value = ThreadLocalRandom.current().ints(valueCount, 0, distinctValueCount).boxed().map(v -> prefix + prefix).collect(Collectors.toList());
            document.addField(indexedProperty, value);
        }
    }


    protected int resolveValueCount(IndexedProperty indexedProperty)
    {
        return ValueProviderParameterUtils.getInt(indexedProperty, "valueCount", 1);
    }


    protected int resolveDistinctValueCount(IndexedProperty indexedProperty)
    {
        return ValueProviderParameterUtils.getInt(indexedProperty, "distinctValueCount", 1);
    }


    protected String resolvePrefix(IndexedProperty indexedProperty)
    {
        return ValueProviderParameterUtils.getString(indexedProperty, "prefix", "value");
    }
}
